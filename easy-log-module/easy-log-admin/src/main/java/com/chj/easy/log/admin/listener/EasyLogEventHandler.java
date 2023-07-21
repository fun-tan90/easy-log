package com.chj.easy.log.admin.listener;

import cn.hutool.json.JSONUtil;
import com.chj.easy.log.core.event.LogAlarmEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * description
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2022/07/13 13:05
 */
@Slf4j
@Async
@Component
public class EasyLogEventHandler {

    @EventListener
    public void logAlarmEvent(LogAlarmEvent event) {
        log.warn("\n{}", JSONUtil.toJsonPrettyStr(event));
    }
}
