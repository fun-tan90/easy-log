package com.chj.easy.log.admin.rest;


import com.chj.easy.log.core.convention.Res;
import com.chj.easy.log.core.model.LogDoc;
import com.chj.easy.log.core.service.EsService;
import com.yomahub.tlog.core.annotation.TLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 11:08
 */
@Slf4j
@RestController
public class DemoController {

    @GetMapping
    @TLogAspect({"id"})
    public Res<String> index(String id) {
//        MDC.put("name","陈浩杰");

        log.error("error is {}", id);
        log.warn("warn is {}", id);
        log.info("warn is {}", id);
        log.debug("debug is {}", id);

        log.error("hello error");
        log.warn("hello warn");
        log.info("hello info");
        log.debug("hello debug");
        return Res.ok("");
    }

    @GetMapping("error-log")
    public Res<String> errorLog() {
        log.error("hello error");
        return Res.ok("");
    }

    @Resource
    private EsService<LogDoc> esService;

    @PostMapping
    public Res<Map<String, List<String>>> aggregation(String indexName, int size, @RequestBody List<String> terms) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        for (String term : terms) {
            searchSourceBuilder.aggregation(AggregationBuilders.terms(term).field(term).size(size));
        }
        return Res.ok(esService.aggregation(indexName, searchSourceBuilder));
    }
}
