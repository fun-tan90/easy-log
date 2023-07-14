package com.chj.easy.log.server.common.service;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 10:59
 */
public interface EsService {

    /**
     * 创建索引
     *
     * @param indexName     索引名称
     * @param indexTemplate 索引模板
     * @return true 成功 false 失败
     */
    boolean createIndex(String indexName, String indexTemplate);
}
