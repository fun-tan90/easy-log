package com.chj.easy.log.server.common.service;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.server.common.convention.page.es.EsPageHelper;
import com.chj.easy.log.server.common.convention.page.es.EsPageInfo;
import com.chj.easy.log.server.common.model.Doc;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Assert.hasLength(indexName, "indexName不能为空");
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        try {
            return restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("exists index failed, {}", e));
        }
    }

    @Override
    public boolean create(String indexName, Class<T> tClass) {
        Assert.hasLength(indexName, "indexName不能为空");
        Assert.notNull(tClass, "tClass不能为空");
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        createIndexRequest.source(ResourceUtil.readUtf8Str(StrUtil.format(EasyLogConstants.INDEX_TEMPLATE_PATH, tClass.getSimpleName())), XContentType.JSON);
        try {
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            return createIndexResponse.isAcknowledged() && createIndexResponse.isShardsAcknowledged();
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("create index failed, {}", e));
        }
    }

    @Override
    public boolean delete(String indexName) {
        Assert.hasLength(indexName, "indexName不能为空");
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
        Assert.hasLength(indexName, "indexName不能为空");
        Assert.hasLength(mappingSource, "mappingSource不能为空");
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
        Assert.hasLength(indexName, "indexName不能为空");
        Assert.notNull(entity, "entity不能为空");
        IndexRequest indexRequest = new IndexRequest(indexName);
        if (StringUtils.hasLength(entity.indexId())) {
            indexRequest.id(entity.indexId());
        }
        indexRequest.source(entity.toSource(), XContentType.JSON);
        try {
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            RestStatus status = indexResponse.status();
            if (Objects.equals(status, RestStatus.CREATED)) {
                entity.setIndexId(indexResponse.getId());
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
        Assert.hasLength(indexName, "indexName不能为空");
        Assert.notEmpty(entities, "entities不能为空");
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
                    entities.get(totalSuccess).setIndexId(next.getId());
                    totalSuccess++;
                }
            }
            return totalSuccess;
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("insertBatch failed, {}", e));
        }
    }


    @Override
    public EsPageInfo<T> paging(String indexName, Integer pageNum, Integer pageSize, SearchSourceBuilder searchSourceBuilder, Class<T> tClass) {
        Assert.hasLength(indexName, "indexName不能为空");
        Assert.notNull(searchSourceBuilder, "查询条件不能为空");
        Assert.notNull(tClass, "tClass不能为空");
        pageNum = pageNum == null || pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize == null || pageSize <= 0 ? 10 : pageSize;

        int from = (pageNum - 1) * pageSize;
        int size = pageSize;

        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);

        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest
                .source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits searchHits = Optional.ofNullable(searchResponse)
                    .map(SearchResponse::getHits)
                    .orElseThrow(() -> new RuntimeException("SearchRequest failed"));
            List<T> rows = Arrays.stream(searchHits.getHits())
                    .map(searchHit -> {
                        T entity = JSONUtil.toBean(searchHit.getSourceAsString(), tClass);
                        entity.setIndexId(searchHit.getId());
                        entity.setHighlight(searchHit.getHighlightFields());
                        return entity;
                    })
                    .collect(Collectors.toList());
            long total = searchHits.getTotalHits().value;
            return EsPageHelper.getPageInfo(rows, total, pageNum, pageSize);
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("search failed, {}", e));
        }
    }
}