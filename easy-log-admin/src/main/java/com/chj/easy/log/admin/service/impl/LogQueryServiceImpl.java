package com.chj.easy.log.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.chj.easy.log.admin.model.cmd.LogQueryBarChartCmd;
import com.chj.easy.log.admin.model.cmd.LogQueryPageCmd;
import com.chj.easy.log.admin.model.cmd.LogQuerySelectCmd;
import com.chj.easy.log.admin.model.vo.LogQueryBarChartVo;
import com.chj.easy.log.admin.service.LogQueryService;
import com.chj.easy.log.core.convention.page.es.EsPageInfo;
import com.chj.easy.log.core.model.Doc;
import com.chj.easy.log.core.model.LogDoc;
import com.chj.easy.log.core.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.LongBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/17 15:36
 */
@Slf4j
@Service
public class LogQueryServiceImpl implements LogQueryService {

    @Resource
    EsService esService;

    @Override
    public Map<String, List<String>> querySelect(LogQuerySelectCmd logQuerySelectCmd) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("appEnv", logQuerySelectCmd.getAppEnv()));
        for (String condition : logQuerySelectCmd.getSelectKeys()) {
            searchSourceBuilder.aggregation(
                    AggregationBuilders
                            .terms(condition)
                            .field(condition)
                            .size(logQuerySelectCmd.getSize())
                            .order(BucketOrder.key(true))
            );
        }
        return esService.aggregation(LogDoc.indexName(logQuerySelectCmd.getDate()), searchSourceBuilder);
    }

    @Override
    public EsPageInfo<Doc> paging(LogQueryPageCmd logQueryPageCmd) {
        SearchSourceBuilder searchSourceBuilder = generateSearchSource(logQueryPageCmd);
        return esService.paging(LogDoc.indexName(logQueryPageCmd.getDate()), logQueryPageCmd.getPageNum(), logQueryPageCmd.getPageSize(), searchSourceBuilder, LogDoc.class);
    }

    @Resource
    RestHighLevelClient restHighLevelClient;

    @Override
    public List<LogQueryBarChartVo> barChart(LogQueryBarChartCmd logQueryBarChartCmd) {
        SearchSourceBuilder searchSourceBuilder = generateSearchSource(logQueryBarChartCmd);
        searchSourceBuilder
                .size(0)
                .aggregation(
                        AggregationBuilders
                                .dateHistogram(logQueryBarChartCmd.getField())
                                .field(logQueryBarChartCmd.getField())
                                .calendarInterval(new DateHistogramInterval(logQueryBarChartCmd.getCalendarInterval()))
                                .offset("+8h")
                                .format(logQueryBarChartCmd.getFormat())
                                .minDocCount(0)
                                .extendedBounds(new LongBounds(DateUtil.parse(logQueryBarChartCmd.getStartDateTime() + ".000").getTime(), DateUtil.parse(logQueryBarChartCmd.getStartDateTime() + ".999").getTime()))
                                .subAggregation(
                                        AggregationBuilders
                                                .terms(logQueryBarChartCmd.getSubField())
                                                .field(logQueryBarChartCmd.getSubField())
                                                .size(5)
                                                .minDocCount(0)
                                                .order(BucketOrder.key(true))
                                )
                );
        SearchRequest searchRequest = new SearchRequest(LogDoc.indexName(logQueryBarChartCmd.getDate()));
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            ParsedDateHistogram dateHistogram = searchResponse.getAggregations().get(logQueryBarChartCmd.getField());
            return dateHistogram.getBuckets().stream().map(bucket -> {
                Terms aggregation = bucket.getAggregations().get(logQueryBarChartCmd.getSubField());
                List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
                List<LogQueryBarChartVo.BarDetail> barDetailList = buckets.stream().map(n -> new LogQueryBarChartVo.BarDetail(n.getKeyAsString(), n.getDocCount())).collect(Collectors.toList());
                return LogQueryBarChartVo.builder()
                        .key(bucket.getKeyAsString())
                        .count(bucket.getDocCount())
                        .barDetailList(barDetailList).build();
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(StrUtil.format("barChart failed, {}", e));
        }
    }
}
