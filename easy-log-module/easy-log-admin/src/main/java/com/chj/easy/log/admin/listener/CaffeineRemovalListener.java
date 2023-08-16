package com.chj.easy.log.admin.listener;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/16 13:11
 */
@Slf4j
@Component
public class CaffeineRemovalListener implements RemovalListener<String,String> {
    @Override
    public void onRemoval(@Nullable String key, @Nullable String value, @NonNull RemovalCause cause) {
        log.debug("key={}, value={}, cause={}", key, value, cause);
    }
}
