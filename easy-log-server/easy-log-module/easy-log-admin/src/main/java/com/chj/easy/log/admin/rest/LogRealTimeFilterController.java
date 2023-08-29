package com.chj.easy.log.admin.rest;

import com.chj.easy.log.admin.model.cmd.LogRealTimeFilterCmd;
import com.chj.easy.log.admin.service.LogRealTimeFilterService;
import com.chj.easy.log.core.convention.Res;
import com.chj.easy.log.core.convention.annotation.Log;
import com.chj.easy.log.core.model.Topic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/21 12:10
 */
@Slf4j
@Log
@RestController
@RequestMapping("log-real-time-filter")
public class LogRealTimeFilterController {

    @Resource
    LogRealTimeFilterService logRealTimeFilterService;

    @PostMapping
    public Res<Topic> subscribe(@RequestBody @Validated LogRealTimeFilterCmd logRealTimeFilterCmd) {
        return Res.ok(logRealTimeFilterService.subscribe(logRealTimeFilterCmd));
    }

    @GetMapping
    public Res<Void> unsubscribe(String mqttClientId) {
        logRealTimeFilterService.unsubscribe(mqttClientId);
        return Res.ok();
    }
}