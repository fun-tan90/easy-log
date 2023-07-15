package com.chj.easy.log.server.common.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
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
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
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
        Assert.hasLength(indexName, "indexName must not be empty");
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        try {
            return restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("exists index failed, {}", e));
        }
    }

    @Override
    public boolean createIndex(String indexName, Class<T> tClass) {
        Assert.hasLength(indexName, "indexName must not be empty");
        Assert.notNull(tClass, "tClass must not be null");
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        createIndexRequest.source(ResourceUtil.readUtf8Str(StrUtil.format(EasyLogConstants.INDEX_TEMPLATE_INIT_FILE, tClass.getSimpleName())), XContentType.JSON);
        try {
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            return createIndexResponse.isAcknowledged() && createIndexResponse.isShardsAcknowledged();
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("create index failed, {}", e));
        }
    }

    @Override
    public boolean updateIndex(String indexName) {
        Assert.hasLength(indexName, "indexName must not be empty");
        PutMappingRequest putMappingRequest = new PutMappingRequest(indexName);
        putMappingRequest.source(ResourceUtil.readUtf8Str(EasyLogConstants.INDEX_TEMPLATE_UPDATE_FILE), XContentType.JSON);
        try {
            AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
            return acknowledgedResponse.isAcknowledged();
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("updateIndex failed, {}", e));
        }
    }

    @Override
    public boolean delete(String indexName) {
        Assert.hasLength(indexName, "indexName must not be empty");
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
        Assert.hasLength(indexName, "indexName must not be empty");
        Assert.hasLength(mappingSource, "mappingSource must not be empty");
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
        Assert.hasLength(indexName, "indexName must not be empty");
        Assert.notNull(entity, "entity must not be null");
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
        Assert.hasLength(indexName, "indexName must not be empty");
        Assert.notEmpty(entities, "entities must not be empty");
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
        Assert.hasLength(indexName, "indexName must not be empty");
        Assert.notNull(searchSourceBuilder, "SearchSourceBuilder must not be null");
        Assert.notNull(tClass, "tClass must not be null");
        pageNum = pageNum == null || pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize == null || pageSize <= 0 ? 10 : pageSize;

        int from = (pageNum - 1) * pageSize;
        int size = pageSize;

        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);


        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            List<T> rows = analyticSearchResponseForHits(searchResponse, tClass);
            long total = analyticSearchResponseForTotalSize(searchResponse);
            return EsPageHelper.getPageInfo(rows, total, pageNum, pageSize);
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("search failed, {}", e));
        }
    }

    private long analyticSearchResponseForTotalSize(SearchResponse searchResponse) {
        SearchHits searchHits = Optional.ofNullable(searchResponse)
                .map(SearchResponse::getHits)
                .orElseThrow(() -> new RuntimeException("SearchRequest failed"));
        return searchHits.getTotalHits().value;
    }

    private List<T> analyticSearchResponseForHits(SearchResponse searchResponse, Class<T> tClass) {
        SearchHits searchHits = Optional.ofNullable(searchResponse)
                .map(SearchResponse::getHits)
                .orElseThrow(() -> new RuntimeException("SearchRequest failed"));
        return Arrays.stream(searchHits.getHits())
                .map(searchHit -> {
                    JSONObject row = JSONUtil.parseObj(searchHit.getSourceAsString());
                    searchHit.getHighlightFields()
                            .forEach((k, v) -> {
                                Optional<Text> fragmentOpt = Arrays.stream(v.getFragments()).findFirst();
                                fragmentOpt.ifPresent(value -> row.putOnce(k, value));
                            });
                    T entity = row.toBean(tClass);
                    entity.setIndexId(searchHit.getId());
                    return entity;
                })
                .collect(Collectors.toList());
    }


    @Override
    public Map<String, List<String>> aggregation(String indexName, SearchSourceBuilder searchSourceBuilder) {
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest
                .source(searchSourceBuilder.size(0));
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            Map<String, Aggregation> aggregationsMap = aggregations.getAsMap();
            System.out.println(aggregationsMap);
            Map<String, List<String>> mapList = new HashMap<>();
            aggregationsMap.forEach((k, v) -> {
                Terms terms = (Terms) v;
                List<? extends Terms.Bucket> buckets = terms.getBuckets();
                List<String> res = buckets.stream().map(MultiBucketsAggregation.Bucket::getKeyAsString).collect(Collectors.toList());
                String name = v.getName();
                mapList.put(name, res);
            });
            return mapList;
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("aggregation failed, {}", e));
        }
    }


    @Override
    public String executeDsl(String indexName, String dsl) {
        Assert.hasLength(indexName, "indexName must not be empty");
        Assert.hasLength(dsl, "dsl must not be empty");
        Request request = new Request("GET", indexName + "/_search");
        request.setJsonEntity(dsl);
        try {
            Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
            return IoUtil.readUtf8(response.getEntity().getContent());
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("executeDsl failed, {}", e));
        }
    }
}