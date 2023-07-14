package com.chj.easy.log.server.collector.rest;


import com.chj.easy.log.common.constant.EasyLogConstants;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 12:32
 */
@RestController
@RequestMapping("es")
public class EsController {

    @Resource
    RestHighLevelClient restHighLevelClient;

    @PutMapping("index")
    public CreateIndexResponse index(String indexName) throws IOException {
        CreateIndexRequest indexRequest = new CreateIndexRequest(indexName);
        indexRequest.source(EasyLogConstants.EASY_LOG_INDEX_MAPPINGS, XContentType.JSON);
        return restHighLevelClient.indices().create(indexRequest, RequestOptions.DEFAULT);
    }
}
