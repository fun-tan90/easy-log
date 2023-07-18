package com.chj.easy.log.admin.rest;

import com.chj.easy.log.admin.model.cmd.LogRealTimeFilterCmd;
import com.chj.easy.log.admin.service.LogRealTimeFilterService;
import com.chj.easy.log.core.convention.Res;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chj
 * @date 2021年07月17日 16:38
 */
@Slf4j
@RestController
@RequestMapping("log-real-time-filter")
public class LogRealTimeFilterController {

    @Resource
    LogRealTimeFilterService logRealTimeFilterService;

    @PostMapping("/subscribe")
    public Res<Void> subscribe(@RequestBody @Validated LogRealTimeFilterCmd logRealTimeFilterCmd) {
        logRealTimeFilterService.subscribe(logRealTimeFilterCmd);
        return Res.ok();
    }
}