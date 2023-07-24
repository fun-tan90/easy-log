package com.chj.easy.log.core.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * description
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2022/09/23 22:21
 */
@Setter
@Getter
public class LogAlarmRegisterEvent extends ApplicationEvent {

    private String clientId;

    private Map<String, String> realTimeFilterRules;

    public LogAlarmRegisterEvent(Object source, String clientId, Map<String, String> realTimeFilterRules) {
        super(source);
        this.clientId = clientId;
        this.realTimeFilterRules = realTimeFilterRules;
    }
}