package com.chj.easy.log.core.appender;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.common.enums.CmdTypeEnum;
import com.chj.easy.log.common.model.CmdDown;
import com.chj.easy.log.common.model.CmdUp;
import com.chj.easy.log.common.model.LogTransferred;
import com.chj.easy.log.common.model.LoggerConfig;
import com.chj.easy.log.common.threadpool.EasyLogThreadPool;
import com.chj.easy.log.common.utils.LocalhostUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttPublishMessage;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.core.client.IMqttClientMessageListener;
import net.dreamlu.iot.mqtt.core.client.MqttClient;
import org.springframework.boot.logging.*;
import org.springframework.util.StringUtils;
import org.tio.core.ChannelContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    private static MqttClient client;

    public static void initMessageChannel() {
        if (MQTT_CLIENT_INITIALIZED.compareAndSet(false, true)) {
            String appName = EasyLogManager.GLOBAL_CONFIG.getAppName();
            String namespace = EasyLogManager.GLOBAL_CONFIG.getNamespace();
            String[] split = EasyLogManager.GLOBAL_CONFIG.getMqttAddress().split(":");
            client = MqttClient.create()
                    .clientId(EasyLogConstants.MQTT_CLIENT_ID_CLIENT_PREFIX + namespace + ":" + appName + ":" + RandomUtil.randomNumbers(6))
                    .ip(split[0])
                    .port(Integer.parseInt(split[1]))
                    .username(EasyLogManager.GLOBAL_CONFIG.getUserName())
                    .password(EasyLogManager.GLOBAL_CONFIG.getPassword())
                    .keepAliveSecs(30)
                    .connectSync();

            client.subQos2(StrUtil.format(EasyLogConstants.MQTT_CMD_DOWN, namespace, appName), new IMqttClientMessageListener() {
                @Override
                public void onSubscribed(ChannelContext context, String topicFilter, MqttQoS mqttQoS) {
                    log.debug("topicFilter:{} MqttQoS:{} 订阅成功！！！", topicFilter, mqttQoS);
                }

                @Override
                public void onMessage(ChannelContext context, String topic, MqttPublishMessage message, byte[] payload) {
                    String msg = new String(payload, StandardCharsets.UTF_8);
                    log.debug("mqtt onMessage {}\n{}", topic, msg);
                    if (!StringUtils.hasLength(msg)) {
                        return;
                    }
                    handlerCmd(topic, msg);
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
                client.publish(StrUtil.format(EasyLogConstants.MQTT_CMD_UP, namespace, appName), JSONUtil.toJsonStr(cmdUp).getBytes(StandardCharsets.UTF_8), MqttQoS.EXACTLY_ONCE);
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
                if (client == null || !client.isConnected()) {
                    return;
                }
                if (logTransferredList.isEmpty()) {
                    blockingQueue.drainTo(logTransferredList, Math.min(blockingQueue.size(), EasyLogManager.GLOBAL_CONFIG.getMaxPushSize()));
                }
                if (logTransferredList.isEmpty()) {
                    return;
                }
                try {
                    client.publish(StrUtil.format(EasyLogConstants.MQTT_LOG, EasyLogManager.GLOBAL_CONFIG.getNamespace(), EasyLogManager.GLOBAL_CONFIG.getAppName()), JSONUtil.toJsonStr(logTransferredList).getBytes(StandardCharsets.UTF_8), MqttQoS.AT_LEAST_ONCE);
                } finally {
                    logTransferredList.clear();
                }
            }, 5, 50, TimeUnit.MILLISECONDS);
        }
    }
}