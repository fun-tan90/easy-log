package com.chj.easy.log.compute;

import com.chj.easy.log.core.model.LogRealTimeFilterRule;
import org.jetlinks.reactor.ql.ReactorQL;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/2 14:56
 */
public class LogRealTimeFilterRulesManager {

    private static final Map<String, ReactorQL> RULES_MAP = new ConcurrentHashMap<>();

    public static Stream<String> stream() {
        return LogRealTimeFilterRulesManager.RULES_MAP.keySet().stream();
    }

    public static ReactorQL getLogRealTimeFilterRule(String clientId) {
        return RULES_MAP.get(clientId);
    }

    public static void putLogRealTimeFilterRule(LogRealTimeFilterRule logRealTimeFilterRule) {
        String sql = logRealTimeFilterRule.getSql();
        RULES_MAP.put(logRealTimeFilterRule.getClientId(), ReactorQL.builder()
                .sql(sql)
                .build());
    }

    public static void removeLogRealTimeFilterRule(LogRealTimeFilterRule logRealTimeFilterRule) {
        RULES_MAP.remove(logRealTimeFilterRule.getClientId());
    }
}
