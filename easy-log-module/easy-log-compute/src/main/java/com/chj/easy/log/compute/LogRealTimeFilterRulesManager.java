package com.chj.easy.log.compute;

import com.chj.easy.log.core.model.LogRealTimeFilterRule;
import reactor.core.publisher.Flux;

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

    public static void main(String[] args) {
        Flux<String> flux = Flux.just("1", "2");
        flux.subscribe(n -> {
            System.out.println("n = " + n);
        }, err -> {
            System.out.println("errorConsumer" + err.getMessage());
        }, () -> {
            System.out.println("completeConsumer");
        });
    }
}
