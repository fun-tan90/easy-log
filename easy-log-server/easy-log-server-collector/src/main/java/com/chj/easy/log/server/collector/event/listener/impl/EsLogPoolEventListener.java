package com.chj.easy.log.server.collector.event.listener.impl;

import com.chj.easy.log.server.collector.event.LogDocPoolEvent;
import com.chj.easy.log.server.collector.event.listener.LogDocPoolEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * description 默认持久化日志监听器
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/6/13 17:52
 */
@Slf4j
@Component
public class EsLogPoolEventListener implements ApplicationListener<LogDocPoolEvent>, LogDocPoolEventListener {
    @Override
    @Async
    public void onApplicationEvent(LogDocPoolEvent event) {
        log.info("EsLogPoolEventListener {}", event.getLogDocs());
    }
}