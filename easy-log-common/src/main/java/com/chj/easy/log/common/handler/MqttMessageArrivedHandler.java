package com.chj.easy.log.common.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.common.enums.CmdTypeEnum;
import com.chj.easy.log.common.model.CmdDown;
import com.chj.easy.log.common.model.CmdUp;
import com.chj.easy.log.common.model.LoggerConfig;
import com.chj.easy.log.common.utils.LocalhostUtil;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.logging.*;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/12 23:56
 */
public class MqttMessageArrivedHandler {

    public static void handlerCmd(String topic, String msg, IMqttAsyncClient client) {
        if (!StringUtils.hasLength(msg)) {
            return;
        }
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
                    client.publish(StrUtil.format(EasyLogConstants.MQTT_CMD_UP_TOPIC, namespace, appName), JSONUtil.toJsonStr(cmdUp).getBytes(StandardCharsets.UTF_8), 1, false);
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
}
