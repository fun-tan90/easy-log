package com.chj.easy.log.admin.handler;

import com.chj.easy.log.admin.service.LogAlarmService;
import com.chj.easy.log.core.event.LogAlarmEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/22 10:25
 */
@Slf4j
@Component
public class LogAlarmEventHandler {

    @Resource
    LogAlarmService logAlarmService;

    @EventListener
    @Async
    public void logAlarmEvent(LogAlarmEvent logAlarmEvent) {
        logAlarmService.handlerLogAlarm(logAlarmEvent.getLogAlarmContent());
    }

}
