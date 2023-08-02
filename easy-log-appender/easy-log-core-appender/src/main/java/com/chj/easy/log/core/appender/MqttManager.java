package com.chj.easy.log.core.appender;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.common.enums.CmdTypeEnum;
import com.chj.easy.log.common.model.CmdDown;
import com.chj.easy.log.common.model.CmdUp;
import com.chj.easy.log.common.model.LoggerConfig;
import com.chj.easy.log.common.utils.LocalhostUtil;
import com.chj.easy.log.core.appender.model.AppBasicInfo;
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
import java.util.List;
import java.util.Optional;
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

    public static void initMessageChannel(AppBasicInfo appBasicInfo, String mqttAddress) {
        if (MQTT_CLIENT_INITIALIZED.compareAndSet(false, true)) {
            String appName = appBasicInfo.getAppName();
            String namespace = appBasicInfo.getNamespace();
            String[] split = mqttAddress.split(":");
            MqttClient client = MqttClient.create()
                    .clientId(EasyLogConstants.MQTT_CLIENT_ID_APP_PREFIX + namespace + ":" + appName + ":" + RandomUtil.randomNumbers(6))
                    .ip(split[0])
                    .port(Integer.parseInt(split[1]))
                    .username(EasyLogConstants.MQTT_CLIENT_USERNAME)
                    .password(EasyLogConstants.MQTT_CLIENT_PASSWORD)
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
                    handlerCmd(appBasicInfo, topic, msg, client);
                }
            });
        }
    }

    private static void handlerCmd(AppBasicInfo appBasicInfo, String topic, String msg, MqttClient client) {
        String appName = appBasicInfo.getAppName();
        String namespace = appBasicInfo.getNamespace();
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
}