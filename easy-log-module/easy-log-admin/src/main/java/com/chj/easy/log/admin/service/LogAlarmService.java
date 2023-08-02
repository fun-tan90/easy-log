package com.chj.easy.log.admin.service;


import com.chj.easy.log.admin.model.cmd.LogAlarmPlatformAddCmd;
import com.chj.easy.log.admin.model.cmd.LogAlarmRuleAddCmd;
import com.chj.easy.log.core.model.LogAlarmContent;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/6/12 22:00
 */
public interface LogAlarmService {

    String logAlarmPlatform(LogAlarmPlatformAddCmd logAlarmPlatformAddCmd);

    String logAlarmRule(LogAlarmRuleAddCmd logAlarmRuleAddCmd);

    void handlerLogAlarm(LogAlarmContent logAlarmContent);
}
