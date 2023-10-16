package fun.tan90.easy.log.compute.loader;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import fun.tan90.easy.log.common.constant.EasyLogConstants;
import fun.tan90.easy.log.core.model.LogAlarmRule;
import com.github.benmanes.caffeine.cache.CacheLoader;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
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
public class LogAlarmRulesCacheLoader implements CacheLoader<String, LogAlarmRule> {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public @Nullable LogAlarmRule load(@NonNull String key) throws Exception {
        log.debug("CaffeineCacheLoader load key={}", key);
        return Optional.ofNullable(StrUtil.emptyToNull(stringRedisTemplate.opsForValue().get(EasyLogConstants.LOG_ALARM_RULES + key)))
                .map(n -> JSONUtil.toBean(n, LogAlarmRule.class))
                .orElse(null);
    }
}