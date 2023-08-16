package com.chj.easy.log.compute;

import cn.hutool.extra.spring.SpringUtil;
import com.chj.easy.log.core.model.LogAlarmRule;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/2 14:56
 */
public class LogAlarmRulesManager {

    private static final LoadingCache<String, Map<String, Map<String, LogAlarmRule>>> LOG_ALARM_RULES_CACHE = SpringUtil.getBean("logAlarmRulesCache");

    /**
     * 缓存日志告警规则
     *
     * @param logAlarmRule
     */
    public static void putLogAlarmRule(LogAlarmRule logAlarmRule) {
        String appName = logAlarmRule.getAppName();
        String namespace = logAlarmRule.getNamespace();
        String loggerName = logAlarmRule.getLoggerName();
        Map<String, Map<String, LogAlarmRule>> namespaceRule = LOG_ALARM_RULES_CACHE.getIfPresent(appName);
        if (CollectionUtils.isEmpty(namespaceRule)) {
            namespaceRule = new HashMap<>();
            Map<String, LogAlarmRule> loggerNameRule = new HashMap<>();
            loggerNameRule.put(loggerName, logAlarmRule);
            namespaceRule.put(namespace, loggerNameRule);
            LOG_ALARM_RULES_CACHE.put(appName, namespaceRule);
        } else {
            Map<String, LogAlarmRule> rule = namespaceRule.get(namespace);
            if (CollectionUtils.isEmpty(rule)) {
                rule = new HashMap<>();
                rule.put(loggerName, logAlarmRule);
                namespaceRule.put(namespace, rule);
            } else {
                rule.put(loggerName, logAlarmRule);
            }
        }
    }

    /**
     * 获取日志告警规则
     *
     * @param appName
     * @param namespace
     * @param loggerName
     * @return
     */
    public static Map<String, LogAlarmRule> getLogAlarmRule(String appName, String namespace, String... loggerName) {
        Map<String, Map<String, LogAlarmRule>> namespaceRule = LOG_ALARM_RULES_CACHE.get(appName);
        if (CollectionUtils.isEmpty(namespaceRule)) {
            return null;
        }
        Map<String, LogAlarmRule> rule = namespaceRule.get(namespace);
        if (CollectionUtils.isEmpty(rule)) {
            return null;
        }
        return Arrays.stream(loggerName).map(rule::get).collect(Collectors.toMap(LogAlarmRule::getLoggerName, Function.identity()));
    }

    public static void removeLogAlarmRule(LogAlarmRule logAlarmRule) {
        String appName = logAlarmRule.getAppName();
        String namespace = logAlarmRule.getNamespace();
        String loggerName = logAlarmRule.getLoggerName();
        Map<String, Map<String, LogAlarmRule>> namespaceRule = LOG_ALARM_RULES_CACHE.get(appName);
        if (!CollectionUtils.isEmpty(namespaceRule)) {
            Map<String, LogAlarmRule> rule = namespaceRule.get(namespace);
            if (!CollectionUtils.isEmpty(rule)) {
                rule.remove(loggerName);
                if (rule.isEmpty()) {
                    namespaceRule.remove(namespace);
                    if (namespaceRule.isEmpty()) {
                        LOG_ALARM_RULES_CACHE.invalidate(appName);
                    }
                }
            }
        }
    }
}
