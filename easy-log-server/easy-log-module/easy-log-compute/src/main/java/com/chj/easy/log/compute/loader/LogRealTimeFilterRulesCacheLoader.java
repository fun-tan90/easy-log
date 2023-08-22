package com.chj.easy.log.compute.loader;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.model.LogRealTimeFilterRule;
import com.github.benmanes.caffeine.cache.CacheLoader;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetlinks.reactor.ql.ReactorQL;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/16 13:13
 */
@Slf4j
@Component
public class LogRealTimeFilterRulesCacheLoader implements CacheLoader<String, ReactorQL> {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public @Nullable ReactorQL load(@NonNull String key) throws Exception {
        log.debug("CaffeineCacheLoader load key={}", key);
        return Optional.ofNullable(StrUtil.emptyToNull(stringRedisTemplate.opsForValue().get(EasyLogConstants.LOG_REAL_TIME_FILTER_RULES + key)))
                .map(n -> ReactorQL.builder().sql(JSONUtil.toBean(n, LogRealTimeFilterRule.class).getSql()).build())
                .orElse(null);
    }
}