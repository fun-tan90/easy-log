package com.chj.easy.log.admin.loader;

import com.github.benmanes.caffeine.cache.CacheLoader;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/16 13:13
 */
@Slf4j
@Component
public class CaffeineCacheLoader implements CacheLoader<String, String> {
    @Override
    public @Nullable String load(@NonNull String key) throws Exception {
        log.debug("CaffeineCacheLoader load key={}", key);
        return "default_" + key;
    }
}