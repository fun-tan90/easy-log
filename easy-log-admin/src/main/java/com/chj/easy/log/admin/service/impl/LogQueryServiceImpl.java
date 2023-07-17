package com.chj.easy.log.admin.service.impl;

import com.chj.easy.log.admin.model.cmd.LogQueryPageCmd;
import com.chj.easy.log.admin.model.cmd.LogQuerySelectCmd;
import com.chj.easy.log.admin.service.LogQueryService;
import com.chj.easy.log.core.convention.page.es.EsPageInfo;
import com.chj.easy.log.core.model.Doc;
import com.chj.easy.log.core.model.LogDoc;
import com.chj.easy.log.core.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
                            .size(100)
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
}
