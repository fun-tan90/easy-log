package com.chj.easy.log.core.event;

import com.chj.easy.log.core.model.LogAlarmRule;
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
public class LogAlarmUnRegisterEvent extends ApplicationEvent {

    private String clientId;

    public LogAlarmUnRegisterEvent(Object source, String clientId) {
        super(source);
        this.clientId = clientId;
    }
}