package com.chj.easy.log.server.common.service;

import com.chj.easy.log.server.common.model.Doc;

import java.util.List;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 10:59
 */
public interface EsService<T extends Doc> {

    /**
     * 创建索引
     *
     * @param indexName     索引名称
     */
    void createIndexIfNotExists(String indexName);

    /**
     * 判断索引是否存在
     * @param indexName 索引名称
     * @return true 存在  false 不存在
     */
    boolean exists(String indexName);

    /**
     * 创建索引
     * @param indexName     索引名称
     * @param tClass 索引模板
     * @return true 成功 false 失败
     */
    boolean create(String indexName, Class<T> tClass);

    /**
     * 删除索引
     * @param indexName 索引名称
     * @return true 成功 false 失败
     */
    boolean delete(String indexName);

    /**
     * 更新索引
     * @param indexName 索引名称
     * @return true 成功 false 失败
     */
    boolean put(String indexName, String mappingSource);

    /**
     * 单条插入
     *
     * @param indexName 索引名称
     * @param entity    单个实体
     * @return 插入条数
     */
    int insertOne(String indexName, T entity);

    /**
     * 批量插入
     *
     * @param indexName 索引名称
     * @param entities  实体集合
     * @return 成功插入条数
     */
    int insertBatch(String indexName, List<T> entities);
}