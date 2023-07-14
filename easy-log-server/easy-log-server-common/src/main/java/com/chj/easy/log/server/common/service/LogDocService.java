package com.chj.easy.log.server.common.service;

import com.chj.easy.log.server.common.model.LogDoc;

import java.util.List;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 10:59
 */
public interface LogDocService {
    void createIndex(String indexName);

    Integer insertBatch(List<LogDoc> entityList, String indexName);
}
