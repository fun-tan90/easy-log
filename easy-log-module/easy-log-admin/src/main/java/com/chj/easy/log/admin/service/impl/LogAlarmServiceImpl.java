package com.chj.easy.log.admin.service.impl;

import cn.hutool.json.JSONUtil;
import com.chj.easy.log.admin.message.MessageCenterServiceChoose;
import com.chj.easy.log.admin.model.cmd.LogAlarmPlatformAddCmd;
import com.chj.easy.log.admin.model.cmd.LogAlarmRuleAddCmd;
import com.chj.easy.log.admin.service.LogAlarmService;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.core.model.LogAlarmContent;
import com.chj.easy.log.core.model.LogAlarmPlatform;
import com.chj.easy.log.core.model.LogAlarmRule;
import com.chj.easy.log.core.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

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

    @Resource
    CacheService cacheService;

    @Resource
    MessageCenterServiceChoose messageCenterServiceChoose;

    @Override
    public String logAlarmPlatform(LogAlarmPlatformAddCmd logAlarmPlatformAddCmd) {
        return cacheService.addAlarmPlatform(logAlarmPlatformAddCmd.getAlarmPlatformType(),
                LogAlarmPlatform.builder()
                        .alarmPlatformName(logAlarmPlatformAddCmd.getAlarmPlatformName())
                        .accessToken(logAlarmPlatformAddCmd.getAccessToken())
                        .secret(logAlarmPlatformAddCmd.getSecret())
                        .build()
        );
    }

    @Override
    public String logAlarmRule(LogAlarmRuleAddCmd logAlarmRuleAddCmd) {
        LogAlarmRule logAlarmRule = LogAlarmRule
                .builder()
                .alarmPlatformType(logAlarmRuleAddCmd.getAlarmPlatformType())
                .alarmPlatformId(logAlarmRuleAddCmd.getAlarmPlatformId())
                .appName(logAlarmRuleAddCmd.getAppName())
                .appEnv(logAlarmRuleAddCmd.getAppEnv())
                .loggerName(StringUtils.hasLength(logAlarmRuleAddCmd.getLoggerName()) ? logAlarmRuleAddCmd.getLoggerName() : "all")
                .receiverList(logAlarmRuleAddCmd.getReceiverList())
                .period(logAlarmRuleAddCmd.getPeriod())
                .threshold(logAlarmRuleAddCmd.getThreshold())
                .status(logAlarmRuleAddCmd.getStatus())
                .build();
        return cacheService.addLogAlarmRule(logAlarmRule);
    }

    @Override
    public void handlerLogAlarm() {
        EasyLogManager.EASY_LOG_SCHEDULED_EXECUTOR.scheduleWithFixedDelay(() -> {
            String logAlarm = cacheService.popLogAlarmContent(5);
            if (StringUtils.hasLength(logAlarm)) {
                LogAlarmContent logAlarmContent = JSONUtil.toBean(logAlarm, LogAlarmContent.class);
                log.debug("\n{}", JSONUtil.toJsonPrettyStr(logAlarmContent));
                messageCenterServiceChoose.execute(logAlarmContent);
            }
        }, 1, 1, TimeUnit.SECONDS);
    }
}
