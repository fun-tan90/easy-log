package com.chj.easy.log.server.admin.listener;

import com.chj.easy.log.server.common.model.LogDoc;
import com.chj.easy.log.server.common.service.LogDocService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 10:15
 */
@Slf4j
@RequiredArgsConstructor
public class AppReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private final LogDocService logDocService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (initialized.compareAndSet(false, true)) {
            logDocService.createIndex(LogDoc.indexName());
        }
    }


}
