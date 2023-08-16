package com.chj.easy.log.compute;

import cn.hutool.extra.spring.SpringUtil;
import com.chj.easy.log.core.model.LogRealTimeFilterRule;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.jetlinks.reactor.ql.ReactorQL;

import java.util.stream.Stream;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/2 14:56
 */
public class LogRealTimeFilterRulesManager {

    private static final LoadingCache<String, ReactorQL> LOG_REAL_TIME_FILTER_RULES_CACHE = SpringUtil.getBean("logRealTimeFilterRulesCache");

    public static Stream<String> stream() {
        return LOG_REAL_TIME_FILTER_RULES_CACHE.asMap().keySet().stream();
    }

    public static ReactorQL getLogRealTimeFilterRule(String clientId) {
        return LOG_REAL_TIME_FILTER_RULES_CACHE.get(clientId);
    }

    public static void putLogRealTimeFilterRule(LogRealTimeFilterRule logRealTimeFilterRule) {
        String sql = logRealTimeFilterRule.getSql();
        LOG_REAL_TIME_FILTER_RULES_CACHE.put(logRealTimeFilterRule.getClientId(), ReactorQL.builder().sql(sql).build());
    }

    public static void removeLogRealTimeFilterRule(String clientId) {
        LOG_REAL_TIME_FILTER_RULES_CACHE.invalidate(clientId);
    }
}
