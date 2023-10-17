package fun.tan90.easy.log.admin.service;


import fun.tan90.easy.log.admin.model.cmd.BaseLogQueryCmd;
import fun.tan90.easy.log.admin.model.cmd.LogDropBoxCmd;
import fun.tan90.easy.log.admin.model.cmd.LogQueryCmd;
import fun.tan90.easy.log.admin.model.cmd.PageParam;
import fun.tan90.easy.log.admin.model.vo.BarChartVo;
import fun.tan90.easy.log.core.convention.exception.ClientException;
import fun.tan90.easy.log.core.convention.page.es.EsPageInfo;
import fun.tan90.easy.log.core.model.Doc;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
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
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        String namespace = logQueryPageCmd.getNamespace();
        boolQueryBuilder.must(QueryBuilders.termQuery("namespace", namespace));

        String startDateTime = logQueryPageCmd.getStartDateTime();
        String endDateTime = logQueryPageCmd.getEndDateTime();
        boolQueryBuilder.must((QueryBuilders.rangeQuery("@timestamp").gte(startDateTime).lt(endDateTime)));

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
            highlightBuilder.field("traceId");
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
            String operator = logQueryPageCmd.getOperator();
            boolQueryBuilder.must(QueryBuilders.matchQuery("content", content).operator(Operator.fromString(operator)));
            highlightBuilder.field("content");
        }

        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.highlighter(highlightBuilder);
        return searchSourceBuilder;
    }

    default void generatePageSearchSource(SearchSourceBuilder searchSourceBuilder, PageParam pageParam) {
        List<String> descList = pageParam.getDescList();
        List<String> ascList = pageParam.getAscList();
        if (!CollectionUtils.isEmpty(descList) && !CollectionUtils.isEmpty(ascList)) {
            if (descList.stream().anyMatch(ascList::contains)) {
                throw new ClientException("升序和降序不能包含同一个字段");
            }
        }
        if (!CollectionUtils.isEmpty(descList)) {
            if (descList.contains("@timestamp")) {
                descList.add("seq");
            }
            for (String desc : descList) {
                searchSourceBuilder.sort(SortBuilders.fieldSort(desc).order(SortOrder.DESC));
            }

        }
        if (!CollectionUtils.isEmpty(ascList)) {
            if (ascList.contains("@timestamp")) {
                ascList.add("seq");
            }
            for (String asc : ascList) {
                searchSourceBuilder.sort(SortBuilders.fieldSort(asc).order(SortOrder.ASC));
            }
        }
        if (CollectionUtils.isEmpty(descList) && CollectionUtils.isEmpty(ascList)) {
            searchSourceBuilder.sort(SortBuilders.fieldSort("@timestamp").order(SortOrder.DESC));
            searchSourceBuilder.sort(SortBuilders.fieldSort("seq").order(SortOrder.DESC));
        }
    }

    /**
     * 查询下拉框
     *
     * @param logDropBoxCmd
     * @return
     */
    Map<String, List<String>> queryDropBox(LogDropBoxCmd logDropBoxCmd);

    /**
     * 分页查询
     *
     * @param logQueryCmd
     * @return
     */
    EsPageInfo<Doc> paging(LogQueryCmd logQueryCmd);

    /**
     * 柱状图
     *
     * @param logQueryCmd
     */
    List<BarChartVo> barChart(LogQueryCmd logQueryCmd);
}
