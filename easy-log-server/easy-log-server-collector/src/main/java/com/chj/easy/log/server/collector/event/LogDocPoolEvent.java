package com.chj.easy.log.server.collector.event;

import com.chj.easy.log.server.common.model.LogDoc;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.List;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 15:53
 */
@Setter
@Getter
public class LogDocPoolEvent extends ApplicationEvent {

    private List<LogDoc> logDocs;

    public LogDocPoolEvent(Object source, List<LogDoc> logDocs) {
        super(source);
        this.logDocs = logDocs;
    }
}
