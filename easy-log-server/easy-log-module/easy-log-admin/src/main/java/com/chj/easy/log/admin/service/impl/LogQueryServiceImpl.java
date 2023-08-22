package com.chj.easy.log.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import com.chj.easy.log.admin.model.cmd.BaseLogQueryCmd;
import com.chj.easy.log.admin.model.cmd.LogDropBoxCmd;
import com.chj.easy.log.admin.model.cmd.LogQueryCmd;
import com.chj.easy.log.admin.model.vo.BarChartVo;
import com.chj.easy.log.admin.service.LogQueryService;
import com.chj.easy.log.core.convention.page.es.EsPageInfo;
import com.chj.easy.log.core.model.Doc;
import com.chj.easy.log.core.model.LogDoc;
import com.chj.easy.log.core.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.LongBounds;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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
    public Map<String, List<String>> queryDropBox(LogDropBoxCmd logDropBoxCmd) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        String namespace = logDropBoxCmd.getNamespace();
        if (StringUtils.hasLength(namespace)) {
            searchSourceBuilder.query(QueryBuilders.termQuery("namespace", namespace));
        }
        for (String condition : logDropBoxCmd.getSelectKeys()) {
            searchSourceBuilder.aggregation(
                    AggregationBuilders
                            .terms(condition)
                            .field(condition)
                            .size(logDropBoxCmd.getSize())
                            .order(BucketOrder.key(true))
            );
        }
        return esService.aggregation(LogDoc.indexName(), searchSourceBuilder);
    }

    @Override
    public EsPageInfo<Doc> paging(LogQueryCmd logQueryCmd) {
        SearchSourceBuilder searchSourceBuilder = generateSearchSource(logQueryCmd.getBaseParam());
        return esService.paging(LogDoc.indexName(), logQueryCmd.getPageParam().getPageNum(), logQueryCmd.getPageParam().getPageSize(), searchSourceBuilder, LogDoc.class);
    }

    @Override
    public List<BarChartVo> barChart(LogQueryCmd logQueryCmd) {
        BaseLogQueryCmd baseParam = logQueryCmd.getBaseParam();
        SearchSourceBuilder searchSourceBuilder = generateSearchSource(baseParam);
        LogQueryCmd.BarChartParam barChartParam = logQueryCmd.getBarChartParam();
        String dateHistogramName = barChartParam.getDateHistogramField();
        String termsName = barChartParam.getTermsField();
        searchSourceBuilder
                .size(0)
                .aggregation(
                        AggregationBuilders
                                .dateHistogram(dateHistogramName)
                                .field(barChartParam.getDateHistogramField())
                                .calendarInterval(new DateHistogramInterval(barChartParam.getCalendarInterval()))
                                .offset("+8h")
                                .format(barChartParam.getFormat())
                                .minDocCount(0)
                                .extendedBounds(new LongBounds(DateUtil.parse(baseParam.getStartDateTime()).getTime(), DateUtil.parse(baseParam.getStartDateTime()).getTime()))
                                .subAggregation(
                                        AggregationBuilders
                                                .terms(termsName)
                                                .field(barChartParam.getTermsField())
                                                .size(5)
                                                .minDocCount(0)
                                                .order(BucketOrder.key(true))
                                )
                );
        List<? extends Histogram.Bucket> histogramBuckets = esService.dateHistogram(LogDoc.indexName(), dateHistogramName, termsName, searchSourceBuilder);
        return histogramBuckets.stream().map(bucket -> {
            Terms aggregation = bucket.getAggregations().get(termsName);
            List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
            List<BarChartVo.OneBarDetail> oneBarDetailList = buckets.stream().map(n -> new BarChartVo.OneBarDetail(n.getKeyAsString(), n.getDocCount())).collect(Collectors.toList());
            return BarChartVo.builder()
                    .key(bucket.getKeyAsString())
                    .count(bucket.getDocCount())
                    .oneBarDetailList(oneBarDetailList).build();
        }).collect(Collectors.toList());
    }
}
