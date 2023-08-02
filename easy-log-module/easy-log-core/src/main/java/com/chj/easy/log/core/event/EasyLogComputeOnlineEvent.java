package com.chj.easy.log.core.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * description
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2022/09/23 22:21
 */
@Setter
@Getter
public class EasyLogComputeOnlineEvent extends ApplicationEvent {

    private String clientId;

    public EasyLogComputeOnlineEvent(Object source, String clientId) {
        super(source);
        this.clientId = clientId;
    }
}