package com.chj.easy.log.server.admin.job;

import com.chj.easy.log.server.common.mapper.LogDocMapper;
import com.chj.easy.log.server.common.model.LogDoc;
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
public class EsIndexJob {

    @Resource
    LogDocMapper logDocMapper;

    @Scheduled(cron = "${easy-log.admin.create-index-cron}")
    public void createIndexTask() {
        String newIndexName = LogDoc.newIndexName();
        if (Boolean.FALSE.equals(logDocMapper.existsIndex(newIndexName))) {
            Boolean createIndex = logDocMapper.createIndex(newIndexName);
            log.info("【{}】索引创建{}", newIndexName, Boolean.TRUE.equals(createIndex) ? "成功" : "失败");
        }
    }
}
