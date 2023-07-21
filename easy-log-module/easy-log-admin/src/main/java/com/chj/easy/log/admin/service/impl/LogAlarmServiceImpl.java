package com.chj.easy.log.admin.service.impl;

import cn.hutool.json.JSONUtil;
import com.chj.easy.log.admin.model.cmd.LogAlarmRuleAddCmd;
import com.chj.easy.log.admin.service.LogAlarmService;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.convention.enums.IErrorCode;
import com.chj.easy.log.core.convention.exception.ClientException;
import com.chj.easy.log.core.model.LogAlarmRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

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
    StringRedisTemplate stringRedisTemplate;

    @Override
    public String logAlarm(LogAlarmRuleAddCmd logAlarmRuleAddCmd) {
        String appName = logAlarmRuleAddCmd.getAppName();
        String appEnv = logAlarmRuleAddCmd.getAppEnv();
        String loggerName = StringUtils.hasLength(logAlarmRuleAddCmd.getLoggerName()) ? logAlarmRuleAddCmd.getLoggerName() : "all";
        String ruleKey = EasyLogConstants.LOG_ALARM_RULES + appName + ":" + appEnv + ":" + loggerName;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(ruleKey))) {
            throw new ClientException(IErrorCode.LOG_ALARM_1004001);
        }
        LogAlarmRule logAlarmRule = LogAlarmRule
                .builder()
                .loggerName(loggerName)
                .receiverList(logAlarmRuleAddCmd.getReceiverList())
                .alarmPlatformId(logAlarmRuleAddCmd.getAlarmPlatformId())
                .period(logAlarmRuleAddCmd.getPeriod())
                .threshold(logAlarmRuleAddCmd.getThreshold())
                .status(logAlarmRuleAddCmd.getStatus())
                .build();
        stringRedisTemplate.opsForValue().set(ruleKey, JSONUtil.toJsonStr(logAlarmRule));
        return ruleKey;
    }
}
