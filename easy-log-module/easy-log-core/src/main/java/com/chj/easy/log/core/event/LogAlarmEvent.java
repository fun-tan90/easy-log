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
public class LogAlarmEvent extends ApplicationEvent {

    private long startTs;

    private long endTs;

    private int times;

    private LogAlarmRule logAlarmRule;

    public LogAlarmEvent(Object source, long startTs, long endTs, int times, LogAlarmRule logAlarmRule) {
        super(source);
        this.startTs = startTs;
        this.endTs = endTs;
        this.times = times;
        this.logAlarmRule = logAlarmRule;
    }
}