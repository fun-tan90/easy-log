package com.chj.easy.log.server.common.service.impl;

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
public class EsServiceImpl extends AbstractEsService {

    @Override
    public void createIndex(String indexName, String indexTemplate) {
        boolean exists = exists(indexName);
        if (!exists) {
            boolean isOk = create(indexName, indexTemplate);
            log.info("【{}】索引创建{}", indexName, isOk ? "成功" : "失败");
        }
        log.info("【{}】索引已存在", indexName);
    }
}
