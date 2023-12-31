package fun.tan90.easy.log.common;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import fun.tan90.easy.log.common.constant.EasyLogConstants;
import fun.tan90.easy.log.common.content.EasyLogConfig;
import fun.tan90.easy.log.common.handler.MqttMessageArrivedHandler;
import fun.tan90.easy.log.common.model.LogTransferred;
import fun.tan90.easy.log.common.model.MeterContext;
import fun.tan90.easy.log.common.threadpool.EasyLogThreadPool;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * description RedisManager
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/20 17:12
 */
@Slf4j(topic = "easy-log")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MqttManager {

    private final static AtomicBoolean MQTT_CLIENT_INITIALIZED = new AtomicBoolean(false);

    private final static AtomicBoolean SCHEDULE_PUSH_LOG_INITIALIZED = new AtomicBoolean(false);

    private static MqttAsyncClient mqttClient;

    @SneakyThrows
    public static void initMessageChannel() {
        if (MQTT_CLIENT_INITIALIZED.compareAndSet(false, true)) {
            String appName = EasyLogManager.GLOBAL_CONFIG.getAppName();
            String namespace = EasyLogManager.GLOBAL_CONFIG.getNamespace();
            String clientId = EasyLogConstants.MQTT_CLIENT_ID_CLIENT_PREFIX + appName + ":" + namespace + ":" + RandomUtil.randomNumbers(4);
            String mqttAddress = EasyLogManager.GLOBAL_CONFIG.getMqttAddress();
            MqttAsyncClient mqttAsyncClient = new MqttAsyncClient(mqttAddress, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(EasyLogManager.GLOBAL_CONFIG.getMqttUserName());
            connOpts.setPassword(EasyLogManager.GLOBAL_CONFIG.getMqttPassword().toCharArray());
            // 设置自动重连
            connOpts.setAutomaticReconnect(true);
            // 设置keepalive
            connOpts.setKeepAliveInterval(30);
            // 设置回调
            mqttAsyncClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.err.println("连接断开，自动重连【" + cause.getMessage() + "】");
                }

                @Override
                public void messageArrived(String topic, org.eclipse.paho.client.mqttv3.MqttMessage message) throws Exception {
                    int qos = message.getQos();
                    String msg = new String(message.getPayload());
                    System.out.println("MQTT主题: " + topic + ", QOS: " + qos + ", payload: " + msg);
                    MqttMessageArrivedHandler.handlerCmd(topic, msg, mqttClient);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }

                @SneakyThrows
                @Override
                public void connectComplete(boolean reconnect, String serverUri) {
                    List<EasyLogConfig.Topic> topics = EasyLogManager.GLOBAL_CONFIG.getTopics();
                    if (!CollectionUtils.isEmpty(topics)) {
                        // 订阅
                        String[] topicFilters = topics.stream().map(EasyLogConfig.Topic::getTopicPattern).toArray(String[]::new);
                        int[] qos = Arrays.stream(topics.stream().map(EasyLogConfig.Topic::getQos).toArray(Integer[]::new)).mapToInt(Integer::valueOf).toArray();
                        mqttAsyncClient.subscribe(topicFilters, qos);
                    }
                }
            });
            mqttAsyncClient.connect(connOpts, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mqttClient = mqttAsyncClient;
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.err.println(exception.getMessage());
                    exception.printStackTrace();
                }
            });
        }
    }

    public static void schedulePushLog(BlockingQueue<LogTransferred> blockingQueue) {
        if (SCHEDULE_PUSH_LOG_INITIALIZED.compareAndSet(false, true)) {
            List<LogTransferred> logTransferredList = new ArrayList<>();
            EasyLogThreadPool.newEasyLogScheduledExecutorInstance().scheduleWithFixedDelay(() -> {
                if (mqttClient == null || !mqttClient.isConnected()) {
                    return;
                }
                if (logTransferredList.isEmpty()) {
                    blockingQueue.drainTo(logTransferredList, Math.min(blockingQueue.size(), EasyLogManager.GLOBAL_CONFIG.getMaxPushSize()));
                }
                if (logTransferredList.isEmpty()) {
                    return;
                }
                try {
                    mqttClient.publish(StrUtil.format(EasyLogConstants.MQTT_LOG_TOPIC, EasyLogManager.GLOBAL_CONFIG.getNamespace(), EasyLogManager.GLOBAL_CONFIG.getAppName()), JSONUtil.toJsonStr(logTransferredList).getBytes(StandardCharsets.UTF_8), 1, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                } finally {
                    logTransferredList.clear();
                }
            }, 5, 50, TimeUnit.MILLISECONDS);
        }
    }

    public static void pushMeter(MeterContext meterContext) {
        log.error(JSONUtil.toJsonStr(meterContext));
        log.warn(JSONUtil.toJsonStr(meterContext));
        log.info(JSONUtil.toJsonStr(meterContext));
        log.debug(JSONUtil.toJsonStr(meterContext));
        log.trace(JSONUtil.toJsonStr(meterContext));
    }
}