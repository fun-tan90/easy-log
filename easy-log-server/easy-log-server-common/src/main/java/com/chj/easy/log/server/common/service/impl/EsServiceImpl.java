package com.chj.easy.log.server.common.service.impl;

import com.chj.easy.log.server.common.service.EsService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 13:09
 */
@Slf4j
@Service
public class EsServiceImpl implements EsService {

    @Resource
    RestHighLevelClient restHighLevelClient;

    @SneakyThrows
    @Override
    public boolean createIndex(String indexName, String indexTemplate) {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        if (!exists) {
            CreateIndexRequest indexRequest = new CreateIndexRequest(indexName);
            indexRequest.source(indexTemplate, XContentType.JSON);
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(indexRequest, RequestOptions.DEFAULT);
            return createIndexResponse.isAcknowledged();
        } else {
            log.warn("【{}】索引已存在", indexName);
            return false;
        }
    }
}
