package com.chj.easy.log.server.common.service;

import cn.hutool.core.util.StrUtil;
import com.chj.easy.log.server.common.model.Doc;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 13:38
 */
@Slf4j
public abstract class AbstractEsService<T extends Doc> implements EsService<T> {

    @Resource
    RestHighLevelClient restHighLevelClient;

    @Override
    public boolean exists(String indexName) {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        try {
            return restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("exists index failed, {}", e));
        }
    }

    @Override
    public boolean create(String indexName, String indexTemplate) {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        createIndexRequest.source(indexTemplate, XContentType.JSON);
        try {
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            return createIndexResponse.isAcknowledged() && createIndexResponse.isShardsAcknowledged();
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("create index failed, {}", e));
        }
    }

    @Override
    public boolean delete(String indexName) {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        try {
            AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            return acknowledgedResponse.isAcknowledged();
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("delete index failed, {}", e));
        }
    }

    @Override
    public boolean put(String indexName, String mappingSource) {
        PutMappingRequest putMappingRequest = new PutMappingRequest(indexName);
        putMappingRequest.source(mappingSource, XContentType.JSON);
        try {
            AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
            return acknowledgedResponse.isAcknowledged();
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("update index failed, {}", e));
        }
    }

    @Override
    public int insertOne(String indexName, T entity) {
        IndexRequest indexRequest = new IndexRequest(indexName);
        if (StringUtils.hasLength(entity.indexId())) {
            indexRequest.id(entity.indexId());
        }
        indexRequest.source(entity.toSource(), XContentType.JSON);
        try {
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            RestStatus status = indexResponse.status();
            if (Objects.equals(status, RestStatus.CREATED)) {
                return 1;
            } else if (Objects.equals(indexResponse.status(), RestStatus.OK)) {
                // 该id已存在,数据被更新的情况
                return 0;
            } else {
                throw new RuntimeException(StrUtil.format("insert failed, result:{} entity:{}", indexResponse.getResult(), entity.toSource()));
            }
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("insertOne failed, {}", e));
        }
    }

    @Override
    public int insertBatch(String indexName, List<T> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return 0;
        }
        BulkRequest bulkRequest = new BulkRequest();
        entities.forEach(entity -> {
            IndexRequest indexRequest = new IndexRequest(indexName);
            if (StringUtils.hasLength(entity.indexId())) {
                indexRequest.id(entity.indexId());
            }
            indexRequest.source(entity.toSource(), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        try {
            BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulkResponse.hasFailures()) {
                log.error(String.join(",", bulkResponse.buildFailureMessage()));
            }
            int totalSuccess = 0;
            for (BulkItemResponse next : bulkResponse) {
                if (Objects.equals(next.status(), RestStatus.CREATED)) {
                    totalSuccess++;
                }
            }
            return totalSuccess;
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("insertBatch failed, {}", e));
        }
    }
}
