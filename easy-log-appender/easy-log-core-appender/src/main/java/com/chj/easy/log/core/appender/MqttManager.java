package com.chj.easy.log.core.appender;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.common.content.EasyLogConfig;
import com.chj.easy.log.common.enums.CmdTypeEnum;
import com.chj.easy.log.common.model.CmdDown;
import com.chj.easy.log.common.model.CmdUp;
import com.chj.easy.log.common.model.LogTransferred;
import com.chj.easy.log.common.model.LoggerConfig;
import com.chj.easy.log.common.threadpool.EasyLogThreadPool;
import com.chj.easy.log.common.utils.LocalhostUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.boot.logging.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * description RedisManager
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/20 17:12
 */
@Slf4j(topic = EasyLogConstants.EASY_LOG_TOPIC)
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
            String clientId = EasyLogConstants.MQTT_CLIENT_ID_CLIENT_PREFIX + namespace + ":" + appName + ":" + RandomUtil.randomNumbers(4);
            String mqttAddress = EasyLogManager.GLOBAL_CONFIG.getMqttAddress();
            MqttAsyncClient client = new MqttAsyncClient(mqttAddress, clientId, new MemoryPersistence());
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(EasyLogManager.GLOBAL_CONFIG.getUserName());
            connOpts.setPassword(EasyLogManager.GLOBAL_CONFIG.getPassword().toCharArray());
            // 关闭自动重连
            connOpts.setAutomaticReconnect(true);
            // 设置keepalive
            connOpts.setKeepAliveInterval(30);
            // 设置回调
            client.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectionLost(Throwable cause) {
                    cause.printStackTrace();
                    System.out.println("连接断开，自动重连【" + cause.getMessage() + "】");
                }

                @Override
                public void messageArrived(String topic, org.eclipse.paho.client.mqttv3.MqttMessage message) throws Exception {
                    int qos = message.getQos();
                    String msg = new String(message.getPayload());
                    System.out.println("接收消息主题: " + topic + ",接收消息Qos:" + qos + ",接收消息: " + msg);
                    if (!StringUtils.hasLength(msg)) {
                        return;
                    }
                    handlerCmd(topic, msg);
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
                        client.subscribe(topicFilters, qos);
                    }
                }
            });
            client.connect(connOpts, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mqttClient = client;
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    exception.printStackTrace();
                }
            });
        }
    }

    private static void handlerCmd(String topic, String msg) {
        String appName = EasyLogManager.GLOBAL_CONFIG.getAppName();
        String namespace = EasyLogManager.GLOBAL_CONFIG.getNamespace();
        if (topic.startsWith(EasyLogConstants.MQTT_CMD_DOWN_PREFIX)) {
            LoggingSystem loggingSystem = SpringUtil.getBean(LoggingSystem.class);
            CmdDown cmdDown = JSONUtil.toBean(msg, CmdDown.class);
            CmdTypeEnum cmdType = cmdDown.getCmdType();
            if (CmdTypeEnum.GET_LOGGER_CONFIGURATIONS.equals(cmdType)) {
                List<LoggerConfiguration> loggerConfigurations = loggingSystem.getLoggerConfigurations();
                List<LoggerConfig> loggerConfigs = loggerConfigurations.stream().map(n -> LoggerConfig.builder()
                        .loggerName(n.getName())
                        .configuredLevel(Optional.ofNullable(n.getConfiguredLevel()).map(Enum::name).orElse("null"))
                        .effectiveLevel(Optional.ofNullable(n.getEffectiveLevel()).map(Enum::name).orElse("null"))
                        .build()).collect(Collectors.toList());
                CmdUp cmdUp = CmdUp.builder()
                        .cmdType(cmdType)
                        .appName(appName)
                        .namespace(namespace)
                        .currIp(LocalhostUtil.getHostIp())
                        .loggerConfigs(loggerConfigs)
                        .build();
                try {
                    mqttClient.publish(StrUtil.format(EasyLogConstants.MQTT_CMD_UP, namespace, appName), JSONUtil.toJsonStr(cmdUp).getBytes(StandardCharsets.UTF_8), 1, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            } else if (CmdTypeEnum.SET_LOGGER_LEVEL_CONFIG.equals(cmdType)) {
                String loggerName = cmdDown.getLoggerName();
                LogLevel logLevel = cmdDown.getLogLevel();
                LoggerGroups loggerGroups = SpringUtil.getBean(LoggerGroups.class);
                LoggerGroup group = loggerGroups.get(loggerName);
                if (group != null && group.hasMembers()) {
                    group.configureLogLevel(logLevel, loggingSystem::setLogLevel);
                    return;
                }
                loggingSystem.setLogLevel(loggerName, logLevel);
            }
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
                    mqttClient.publish(StrUtil.format(EasyLogConstants.MQTT_LOG, EasyLogManager.GLOBAL_CONFIG.getNamespace(), EasyLogManager.GLOBAL_CONFIG.getAppName()), JSONUtil.toJsonStr(logTransferredList).getBytes(StandardCharsets.UTF_8), 1, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                } finally {
                    logTransferredList.clear();
                }
            }, 5, 50, TimeUnit.MILLISECONDS);
        }
    }
}