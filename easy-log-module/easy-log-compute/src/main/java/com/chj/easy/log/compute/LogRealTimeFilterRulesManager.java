package com.chj.easy.log.compute;

import com.chj.easy.log.core.model.LogRealTimeFilterRule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/2 14:56
 */
public class LogRealTimeFilterRulesManager {

    public static final Map<String, Map<String, String>> RULES_MAP = new ConcurrentHashMap<>();

    public static void putLogRealTimeFilterRule(LogRealTimeFilterRule logRealTimeFilterRule) {
        RULES_MAP.put(logRealTimeFilterRule.getClientId(), logRealTimeFilterRule.getRealTimeFilterRules());
    }

    public static void removeLogRealTimeFilterRule(LogRealTimeFilterRule logRealTimeFilterRule) {
        RULES_MAP.remove(logRealTimeFilterRule.getClientId());
    }
}
