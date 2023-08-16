package com.chj.easy.log.admin.config;

import com.github.benmanes.caffeine.cache.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:50
 */
@Slf4j
@ConditionalOnBean(EasyLogAdminAutoConfiguration.class)
public class EasyLogCacheAutoConfiguration {

    @Bean
    public LoadingCache<String, String> loadingCache(RemovalListener<String, String> removalListener, CacheLoader<String, String> cacheLoader) {
        return Caffeine.newBuilder()
                .scheduler(Scheduler.forScheduledExecutorService(Executors.newScheduledThreadPool(1)))
                .maximumSize(10_000)
                // 自上一次写入或者读取缓存开始，在经过指定时间之后过期。
                .expireAfterAccess(5, TimeUnit.SECONDS)
                // 自缓存生成后，经过指定时间或者一次替换值之后过期。
                .expireAfterWrite(15, TimeUnit.SECONDS)
                .refreshAfterWrite(10, TimeUnit.MINUTES)
                .recordStats()  // 记录统计信息
                .removalListener(removalListener)
                .build(cacheLoader);
    }
}