package com.chj.easy.log.compute.loader;

import com.chj.easy.log.core.model.LogAlarmRule;
import com.github.benmanes.caffeine.cache.CacheLoader;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/16 13:13
 */
@Slf4j
@Component
public class LogAlarmRulesCacheLoader implements CacheLoader<String, Map<String, Map<String, LogAlarmRule>>> {
    @Override
    public @Nullable Map<String, Map<String, LogAlarmRule>> load(@NonNull String key) throws Exception {
        log.debug("CaffeineCacheLoader load key={}", key);
        return null;
    }
}