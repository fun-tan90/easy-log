package com.chj.easy.log.admin.handler;

import com.chj.easy.log.core.event.LogRealTimeFilterUnRegisterEvent;
import com.chj.easy.log.core.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/22 10:25
 */
@Slf4j
@Component
public class LogRealTimeFilterEventHandler {

    @Resource
    CacheService cacheService;

    @EventListener
    public void logRealTimeFilterUnRegisterEvent(LogRealTimeFilterUnRegisterEvent logRealTimeFilterUnRegisterEvent) {
        String clientId = logRealTimeFilterUnRegisterEvent.getClientId();
        cacheService.delLogRealTimeFilterRule(clientId);
    }
}
