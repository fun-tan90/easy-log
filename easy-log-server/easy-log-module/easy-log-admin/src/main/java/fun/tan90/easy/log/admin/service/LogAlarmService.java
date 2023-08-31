package fun.tan90.easy.log.admin.service;


import fun.tan90.easy.log.admin.model.cmd.LogAlarmPlatformAddCmd;
import fun.tan90.easy.log.admin.model.cmd.LogAlarmRuleAddCmd;
import fun.tan90.easy.log.core.model.LogAlarmContent;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/6/12 22:00
 */
public interface LogAlarmService {

    String addLogAlarmPlatform(LogAlarmPlatformAddCmd logAlarmPlatformAddCmd);

    String addLogAlarmRule(LogAlarmRuleAddCmd logAlarmRuleAddCmd);

    void handlerLogAlarm(LogAlarmContent logAlarmContent);
}
