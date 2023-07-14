package com.chj.easy.log.server.common.service;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 13:38
 */
@Slf4j
public abstract class AbstractEsService implements EsService {

    @Resource
    RestHighLevelClient restHighLevelClient;

    /**
     * 索引是否存在
     *
     * @param indexName 索引名称
     * @return 成功、失败
     */
    public boolean exists(String indexName) {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        try {
            return restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("判断索引是否存在异常", e);
            return false;
        }
    }

    /**
     * 创建索引
     *
     * @param indexName     索引名称
     * @param indexTemplate 索引模板
     * @return 成功、失败
     */
    public boolean create(String indexName, String indexTemplate) {
        CreateIndexRequest indexRequest = new CreateIndexRequest(indexName);
        indexRequest.source(indexTemplate, XContentType.JSON);
        try {
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(indexRequest, RequestOptions.DEFAULT);
            return createIndexResponse.isAcknowledged() && createIndexResponse.isShardsAcknowledged();
        } catch (IOException e) {
            log.error("创建索引异常", e);
            return false;
        }
    }

}
