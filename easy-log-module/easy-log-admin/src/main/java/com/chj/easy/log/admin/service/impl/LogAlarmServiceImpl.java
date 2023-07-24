package com.chj.easy.log.admin.service.impl;

import cn.hutool.json.JSONUtil;
import com.chj.easy.log.admin.model.cmd.LogAlarmRuleAddCmd;
import com.chj.easy.log.admin.service.LogAlarmService;
import com.chj.easy.log.common.constant.EasyLogConstants;
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
        String ruleKey = EasyLogConstants.LOG_ALARM_RULES + appName + ":" + appEnv;
        String loggerName = StringUtils.hasLength(logAlarmRuleAddCmd.getLoggerName()) ? logAlarmRuleAddCmd.getLoggerName() : "all";
        LogAlarmRule logAlarmRule = LogAlarmRule
                .builder()
                .loggerName(loggerName)
                .receiverList(logAlarmRuleAddCmd.getReceiverList())
                .alarmPlatformId(logAlarmRuleAddCmd.getAlarmPlatformId())
                .period(logAlarmRuleAddCmd.getPeriod())
                .threshold(logAlarmRuleAddCmd.getThreshold())
                .status(logAlarmRuleAddCmd.getStatus())
                .build();
        stringRedisTemplate.opsForHash().put(ruleKey, loggerName, JSONUtil.toJsonStr(logAlarmRule));
        return ruleKey;
    }
}
