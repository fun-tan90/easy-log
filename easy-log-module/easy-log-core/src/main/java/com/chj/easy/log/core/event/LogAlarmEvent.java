package com.chj.easy.log.core.event;

import com.chj.easy.log.core.model.LogAlarmContent;
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
public class LogAlarmEvent extends ApplicationEvent {

    private LogAlarmContent logAlarmContent;

    public LogAlarmEvent(Object source, LogAlarmContent logAlarmContent) {
        super(source);
        this.logAlarmContent = logAlarmContent;
    }
}