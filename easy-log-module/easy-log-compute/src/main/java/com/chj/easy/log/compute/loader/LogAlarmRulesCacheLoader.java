package com.chj.easy.log.compute.loader;

import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.model.LogAlarmRule;
import com.github.benmanes.caffeine.cache.CacheLoader;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/16 13:13
 */
@Slf4j
@Component
public class LogAlarmRulesCacheLoader implements CacheLoader<String, LogAlarmRule> {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public @Nullable LogAlarmRule load(@NonNull String key) throws Exception {
        log.debug("CaffeineCacheLoader load key={}", key);
        String logAlarmRuleJson = stringRedisTemplate.opsForValue().get(EasyLogConstants.LOG_ALARM_RULES + key);
        if (StringUtils.hasLength(logAlarmRuleJson)) {
            return JSONUtil.toBean(logAlarmRuleJson, LogAlarmRule.class);
        }
        return null;
    }
}