package com.chj.easy.log.compute.config;

import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.compute.loader.LogAlarmRulesCacheLoader;
import com.chj.easy.log.compute.loader.LogRealTimeFilterRulesCacheLoader;
import com.chj.easy.log.compute.property.EasyLogComputeProperties;
import com.chj.easy.log.core.model.LogAlarmRule;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.Scheduler;
import org.jetlinks.reactor.ql.ReactorQL;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:50
 */
@ConditionalOnProperty(value = "easy-log.compute.enable", havingValue = "true")
@ComponentScan(EasyLogConstants.COMPUTE_SCAN_BASE_PACKAGES)
@EnableConfigurationProperties(EasyLogComputeProperties.class)
public class EasyLogComputeAutoConfiguration {

    @Bean
    public LoadingCache<String, LogAlarmRule> logAlarmRulesCache(LogAlarmRulesCacheLoader cacheLoader) {
        return Caffeine.newBuilder()
                .scheduler(Scheduler.forScheduledExecutorService(Executors.newScheduledThreadPool(1)))
                .maximumSize(10_000)
                // 自上一次写入或者读取缓存开始，在经过指定时间之后过期。
                .expireAfterAccess(5, TimeUnit.SECONDS)
                // 自缓存生成后，经过指定时间或者一次替换值之后过期。
                // .expireAfterWrite(15, TimeUnit.SECONDS)
                .recordStats()  // 记录统计信息
                .build(cacheLoader);
    }

    @Bean
    public LoadingCache<String, ReactorQL> logRealTimeFilterRulesCache(LogRealTimeFilterRulesCacheLoader cacheLoader) {
        return Caffeine.newBuilder()
                .scheduler(Scheduler.forScheduledExecutorService(Executors.newScheduledThreadPool(1)))
                .maximumSize(10_000)
                // 自上一次写入或者读取缓存开始，在经过指定时间之后过期。
                .expireAfterAccess(5, TimeUnit.SECONDS)
                // 自缓存生成后，经过指定时间或者一次替换值之后过期。
                // .expireAfterWrite(15, TimeUnit.SECONDS)
                .recordStats()  // 记录统计信息
                .build(cacheLoader);
    }

}
