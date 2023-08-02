package com.chj.easy.log.admin.handler;

import com.chj.easy.log.core.event.EasyLogComputeOnlineEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/22 10:25
 */
@Slf4j
@Component
public class EasyLogComputeOnlineEventHandler {

    @EventListener
    @Async
    public void easyLogComputeOnlineEvent(EasyLogComputeOnlineEvent easyLogComputeOnlineEvent) {

        // TODO 重新发送 日志告警规则
        // TODO 重新发送 实时过滤规则
    }

}
