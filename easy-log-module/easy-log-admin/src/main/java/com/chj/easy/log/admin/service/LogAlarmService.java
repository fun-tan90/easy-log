package com.chj.easy.log.admin.service;


import com.chj.easy.log.admin.model.cmd.LogAlarmRuleAddCmd;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/6/12 22:00
 */
public interface LogAlarmService {

    String logAlarm(LogAlarmRuleAddCmd logAlarmRuleAddCmd);

    void handlerLogAlarm();
}
