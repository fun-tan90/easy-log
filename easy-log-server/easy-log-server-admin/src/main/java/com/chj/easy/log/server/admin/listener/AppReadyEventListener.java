package com.chj.easy.log.server.admin.listener;

import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.server.common.model.LogDoc;
import com.chj.easy.log.server.common.service.EsService;
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

    private final EsService<LogDoc> esService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (initialized.compareAndSet(false, true)) {
            esService.createIndexIfNotExists(LogDoc.indexName(), EasyLogConstants.EASY_LOG_INDEX_MAPPINGS);
        }
    }


}
