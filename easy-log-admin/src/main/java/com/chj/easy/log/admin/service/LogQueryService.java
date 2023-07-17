package com.chj.easy.log.admin.service;


import cn.hutool.json.JSONUtil;
import com.chj.easy.log.admin.model.cmd.BaseLogQueryCmd;
import com.chj.easy.log.admin.model.cmd.LogQueryPageCmd;
import com.chj.easy.log.admin.model.cmd.LogQuerySelectCmd;
import com.chj.easy.log.core.convention.page.es.EsPageInfo;
import com.chj.easy.log.core.model.Doc;
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
        String appEnv = logQueryPageCmd.getAppEnv();
        searchSourceBuilder.query(QueryBuilders.termQuery("appEnv", appEnv));
        String startDateTime = logQueryPageCmd.getStartDateTime();
        String endDateTime = logQueryPageCmd.getEndDateTime();
        searchSourceBuilder.query(QueryBuilders.rangeQuery("dateTime").gte(startDateTime).lt(endDateTime));

        List<String> appNameList = logQueryPageCmd.getAppNameList();
        if (!CollectionUtils.isEmpty(appNameList)) {
            for (String appName : appNameList) {
                searchSourceBuilder.query(QueryBuilders.boolQuery().should(QueryBuilders.termQuery("appName", appName)));
            }
        }

        List<String> levelList = logQueryPageCmd.getLevelList();
        if (!CollectionUtils.isEmpty(levelList)) {
            for (String level : levelList) {
                searchSourceBuilder.query(QueryBuilders.boolQuery().should(QueryBuilders.termQuery("level", level)));
            }
        }
        String traceId = logQueryPageCmd.getTraceId();
        if (StringUtils.hasLength(traceId)) {
            searchSourceBuilder.query(QueryBuilders.termQuery("traceId", traceId));
        }
        String loggerName = logQueryPageCmd.getLoggerName();
        if (StringUtils.hasLength(traceId)) {
            searchSourceBuilder.query(QueryBuilders.termQuery("loggerName", loggerName));
        }
        String lineNumber = logQueryPageCmd.getLineNumber();
        if (StringUtils.hasLength(lineNumber)) {
            searchSourceBuilder.query(QueryBuilders.termQuery("lineNumber", lineNumber));
        }
        List<String> ipList = logQueryPageCmd.getIpList();
        if (!CollectionUtils.isEmpty(ipList)) {
            for (String ip : ipList) {
                searchSourceBuilder.query(QueryBuilders.boolQuery().should(QueryBuilders.termQuery("currIp", ip)));
            }
        }
        String content = logQueryPageCmd.getContent();
        if (StringUtils.hasLength(content)) {
            searchSourceBuilder.query(QueryBuilders.matchQuery("content", content));
        }

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
        System.out.println(JSONUtil.toJsonPrettyStr(searchSourceBuilder.toString()));
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
}
