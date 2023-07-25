package com.chj.easy.log.admin.rest;

import com.chj.easy.log.admin.model.cmd.LogAlarmPlatformAddCmd;
import com.chj.easy.log.admin.model.cmd.LogAlarmRuleAddCmd;
import com.chj.easy.log.admin.service.LogAlarmService;
import com.chj.easy.log.core.convention.Res;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/21 12:10
 */
@Slf4j
@RestController
@RequestMapping("log-alarm")
public class LogAlarmController {

    @Resource
    LogAlarmService logAlarmService;

    @PostMapping("platform")
    public Res<String> logAlarmPlatform(@RequestBody @Validated LogAlarmPlatformAddCmd logAlarmPlatformAddCmd) {
        return Res.ok(logAlarmService.logAlarmPlatform(logAlarmPlatformAddCmd));
    }

    @PostMapping("rule")
    public Res<String> logAlarmRule(@RequestBody @Validated LogAlarmRuleAddCmd logAlarmRuleAddCmd) {
        return Res.ok(logAlarmService.logAlarmRule(logAlarmRuleAddCmd));
    }
}