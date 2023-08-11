package com.chj.easy.log.admin.service.impl;

import cn.hutool.json.JSONUtil;
import com.chj.easy.log.admin.message.MessageCenterServiceChoose;
import com.chj.easy.log.admin.model.cmd.LogAlarmPlatformAddCmd;
import com.chj.easy.log.admin.model.cmd.LogAlarmRuleAddCmd;
import com.chj.easy.log.admin.service.LogAlarmService;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.model.LogAlarmContent;
import com.chj.easy.log.core.model.LogAlarmPlatform;
import com.chj.easy.log.core.model.LogAlarmRule;
import com.chj.easy.log.core.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.spring.client.MqttClientTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/20 10:25
 */
@Slf4j
@Service
public class LogAlarmServiceImpl implements LogAlarmService {

    @Resource
    CacheService cacheService;

    @Resource
    MessageCenterServiceChoose messageCenterServiceChoose;

    @Resource
    MqttClientTemplate mqttClientTemplate;

    @Override
    public String addLogAlarmPlatform(LogAlarmPlatformAddCmd logAlarmPlatformAddCmd) {
        return cacheService.addAlarmPlatform(logAlarmPlatformAddCmd.getAlarmPlatformType(),
                LogAlarmPlatform.builder()
                        .alarmPlatformName(logAlarmPlatformAddCmd.getAlarmPlatformName())
                        .accessToken(logAlarmPlatformAddCmd.getAccessToken())
                        .secret(logAlarmPlatformAddCmd.getSecret())
                        .status(logAlarmPlatformAddCmd.getStatus())
                        .build()
        );
    }

    @Override
    public String addLogAlarmRule(LogAlarmRuleAddCmd logAlarmRuleAddCmd) {
        LogAlarmRule logAlarmRule = LogAlarmRule
                .builder()
                .alarmPlatformType(logAlarmRuleAddCmd.getAlarmPlatformType())
                .alarmPlatformId(logAlarmRuleAddCmd.getAlarmPlatformId())
                .appName(logAlarmRuleAddCmd.getAppName())
                .namespace(logAlarmRuleAddCmd.getNamespace())
                .loggerName(StringUtils.hasLength(logAlarmRuleAddCmd.getLoggerName()) ? logAlarmRuleAddCmd.getLoggerName() : "all")
                .receiverList(logAlarmRuleAddCmd.getReceiverList())
                .period(logAlarmRuleAddCmd.getPeriod())
                .threshold(logAlarmRuleAddCmd.getThreshold())
                .status(logAlarmRuleAddCmd.getStatus())
                .build();
        mqttClientTemplate.publish(EasyLogConstants.MQTT_LOG_ALARM_RULES_TOPIC + "put", JSONUtil.toJsonStr(logAlarmRule).getBytes(StandardCharsets.UTF_8), MqttQoS.EXACTLY_ONCE);
        return cacheService.addLogAlarmRule(logAlarmRule);
    }

    @Override
    public void handlerLogAlarm(LogAlarmContent logAlarmContent) {
        if (Objects.isNull(logAlarmContent)) {
            return;
        }
        log.debug("告警信息:\n{}", JSONUtil.toJsonPrettyStr(logAlarmContent));
        messageCenterServiceChoose.execute(logAlarmContent);
    }
}
