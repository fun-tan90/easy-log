package com.chj.easy.log.core.service;

import com.chj.easy.log.core.convention.page.es.EsPageInfo;
import com.chj.easy.log.core.model.Doc;
import com.chj.easy.log.core.model.LogDoc;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;
import java.util.Map;

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
     * @param indexName 索引名称
     */
    void createIndexIfNotExists(String indexName);

    /**
     * 判断索引是否存在
     *
     * @param indexName 索引名称
     * @return true 存在  false 不存在
     */
    boolean exists(String indexName);

    /**
     * 创建索引
     *
     * @param indexName 索引名称
     * @param mappings  索引模板
     * @return true 成功 false 失败
     */
    boolean createIndex(String indexName, String mappings);

    /**
     * 更新索引
     *
     * @param indexName
     * @return
     */
    boolean updateIndex(String indexName, String mappings);

    /**
     * 删除索引
     *
     * @param indexName 索引名称
     * @return true 成功 false 失败
     */
    boolean delete(String indexName);

    /**
     * 更新索引
     *
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
    int insertOne(String indexName, Doc entity);

    /**
     * 批量插入
     *
     * @param indexName 索引名称
     * @param entities  实体集合
     * @return 成功插入条数
     */
    int insertBatch(String indexName, List<Doc> entities);

    /**
     * 分页查询
     *
     * @param indexName           索引名称
     * @param pageNum             当前页
     * @param pageSize            每页条数
     * @param searchSourceBuilder 条件
     * @param tClass              实体类型
     * @return 指定的返回类型
     */
    EsPageInfo<Doc> paging(String indexName, Integer pageNum, Integer pageSize, SearchSourceBuilder searchSourceBuilder, Class<? extends Doc> tClass);


    /**
     * 多条件聚合查询
     *
     * @param indexName
     * @param searchSourceBuilder
     * @return
     */
    Map<String, List<String>> aggregation(String indexName, SearchSourceBuilder searchSourceBuilder);

    /**
     * 原生查询
     *
     * @param indexName 索引名称
     * @param dsl       dsl语句
     * @return 执行结果
     */
    String executeSearchDsl(String indexName, String dsl);
}