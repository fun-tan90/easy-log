package com.chj.easy.log.server.common.service.impl;

import com.chj.easy.log.server.common.model.LogDoc;
import com.chj.easy.log.server.common.service.AbstractEsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 13:09
 */
@Slf4j
@Service
public class LogDocEsServiceImpl extends AbstractEsService<LogDoc> {

    @Override
    public void createIndexIfNotExists(String indexName) {
        boolean exists = exists(indexName);
        if (!exists) {
            boolean createIndex = createIndex(indexName, LogDoc.class);
            log.info("【{}】索引创建{}", indexName, createIndex ? "成功" : "失败");
        } else {
            log.info("【{}】索引已存在", indexName);
        }
        boolean updateIndex = updateIndex(indexName);
        log.info("【{}】索引字段添加{}", indexName, updateIndex ? "成功" : "失败");
    }
}
