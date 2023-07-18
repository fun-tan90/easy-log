package com.chj.easy.log.admin.service;


import com.chj.easy.log.admin.model.cmd.BaseLogQueryCmd;
import com.chj.easy.log.admin.model.cmd.LogQueryBarChartCmd;
import com.chj.easy.log.admin.model.cmd.LogQueryPageCmd;
import com.chj.easy.log.admin.model.cmd.LogQuerySelectCmd;
import com.chj.easy.log.core.convention.page.es.EsPageInfo;
import com.chj.easy.log.core.model.Doc;
import com.chj.easy.log.core.model.vo.BarChartVo;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/6/12 22:00
 */
public interface LogQueryService {

    default SearchSourceBuilder generateSearchSource(BaseLogQueryCmd logQueryPageCmd) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        String appEnv = logQueryPageCmd.getAppEnv();
        boolQueryBuilder.must(QueryBuilders.termQuery("appEnv", appEnv));

        String startDateTime = logQueryPageCmd.getStartDateTime();
        String endDateTime = logQueryPageCmd.getEndDateTime();
        boolQueryBuilder.must((QueryBuilders.rangeQuery("dateTime").gte(startDateTime + ".000").lt(endDateTime + ".999")));

        List<String> appNameList = logQueryPageCmd.getAppNameList();
        if (!CollectionUtils.isEmpty(appNameList)) {
            BoolQueryBuilder appNameBool = QueryBuilders.boolQuery();
            for (String appName : appNameList) {
                appNameBool.should(QueryBuilders.termQuery("appName", appName));
            }
            boolQueryBuilder.must(appNameBool);
        }

        List<String> levelList = logQueryPageCmd.getLevelList();
        if (!CollectionUtils.isEmpty(levelList)) {
            BoolQueryBuilder levelBool = QueryBuilders.boolQuery();
            for (String level : levelList) {
                levelBool.should(QueryBuilders.termQuery("level", level));
            }
            boolQueryBuilder.must(levelBool);
        }
        String traceId = logQueryPageCmd.getTraceId();
        if (StringUtils.hasLength(traceId)) {
            boolQueryBuilder.must(QueryBuilders.termQuery("traceId", traceId));
        }
        String loggerName = logQueryPageCmd.getLoggerName();
        if (StringUtils.hasLength(loggerName)) {
            boolQueryBuilder.must(QueryBuilders.termQuery("loggerName", loggerName));
        }
        String lineNumber = logQueryPageCmd.getLineNumber();
        if (StringUtils.hasLength(lineNumber)) {
            boolQueryBuilder.must(QueryBuilders.termQuery("lineNumber", lineNumber));
        }
        List<String> ipList = logQueryPageCmd.getIpList();
        if (!CollectionUtils.isEmpty(ipList)) {
            BoolQueryBuilder ipBool = QueryBuilders.boolQuery();
            for (String ip : ipList) {
                ipBool.should(QueryBuilders.termQuery("currIp", ip));
            }
            boolQueryBuilder.must(ipBool);
        }
        String content = logQueryPageCmd.getContent();
        if (StringUtils.hasLength(content)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("content", content));
        }

        searchSourceBuilder.query(boolQueryBuilder);

        List<String> descList = logQueryPageCmd.getDescList();
        if (!CollectionUtils.isEmpty(descList)) {
            for (String desc : descList) {
                searchSourceBuilder.sort(SortBuilders.fieldSort(desc).order(SortOrder.DESC));
            }
        }
        List<String> ascList = logQueryPageCmd.getAscList();
        if (!CollectionUtils.isEmpty(ascList)) {
            for (String asc : ascList) {
                searchSourceBuilder.sort(SortBuilders.fieldSort(asc).order(SortOrder.ASC));
            }
        }
        System.out.println(searchSourceBuilder);
        return searchSourceBuilder;
    }

    /**
     * 查询下拉框
     *
     * @param logQuerySelectCmd
     * @return
     */
    Map<String, List<String>> querySelect(LogQuerySelectCmd logQuerySelectCmd);

    /**
     * 分页查询
     *
     * @param logQueryPageCmd
     * @return
     */
    EsPageInfo<Doc> paging(LogQueryPageCmd logQueryPageCmd);

    /**
     * 柱状图
     * @param logQueryBarChartCmd
     */
    List<BarChartVo> barChart(LogQueryBarChartCmd logQueryBarChartCmd);
}
