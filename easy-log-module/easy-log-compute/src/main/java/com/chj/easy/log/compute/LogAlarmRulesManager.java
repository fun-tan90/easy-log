package com.chj.easy.log.compute;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.chj.easy.log.core.model.LogAlarmRule;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/2 14:56
 */
public class LogAlarmRulesManager {

    private static final LoadingCache<String, LogAlarmRule> LOG_ALARM_RULES_CACHE = SpringUtil.getBean("logAlarmRulesCache");

    /**
     * 缓存日志告警规则
     *
     * @param logAlarmRule
     */
    public static void putLogAlarmRule(LogAlarmRule logAlarmRule) {
        LOG_ALARM_RULES_CACHE.put(logAlarmRule.getRuleId(), logAlarmRule);
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
        List<String> cacheKeys = Arrays.stream(loggerName).map(n -> SecureUtil.md5(appName + ":" + namespace + ":" + n)).collect(Collectors.toList());
        return LOG_ALARM_RULES_CACHE.getAll(cacheKeys);
    }

    public static void removeLogAlarmRule(String ruleId) {
        LOG_ALARM_RULES_CACHE.invalidate(ruleId);
    }
}
