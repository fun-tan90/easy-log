package com.chj.easy.log.server.admin.job;

import com.chj.easy.log.server.common.model.LogDoc;
import com.chj.easy.log.server.common.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 17:34
 */
@Slf4j
@Component
public class EsIndexInitJob {

    @Resource
    EsService<LogDoc> esService;

    @Scheduled(cron = "${easy-log.admin.init-index-cron}")
    public void createLogDocIndexTask() {
        String newIndexName = LogDoc.newIndexName();
        esService.createIndexIfNotExists(newIndexName);
    }
}