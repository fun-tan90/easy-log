package com.chj.easy.log.core.service.impl;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.lang.id.NanoId;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.model.LogAlarmPlatform;
import com.chj.easy.log.core.model.LogAlarmRule;
import com.chj.easy.log.core.model.LogRealTimeFilterRule;
import com.chj.easy.log.core.model.SlidingWindow;
import com.chj.easy.log.core.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/18 20:33
 */
@Slf4j(topic = EasyLogConstants.EASY_LOG_TOPIC)
@Service
public class CacheServiceImpl implements CacheService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public SlidingWindow slidingWindow(String key, String unique, long timestamp, int period) {
        DefaultRedisScript<String> actual = Singleton.get(EasyLogConstants.SLIDING_WINDOW_LUA_PATH, () -> {
            DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(EasyLogConstants.SLIDING_WINDOW_LUA_PATH)));
            redisScript.setResultType(String.class);
            return redisScript;
        });
        String execute = stringRedisTemplate.execute(actual, Collections.singletonList(key), String.valueOf(period), String.valueOf(timestamp), unique);
        if (StringUtils.hasLength(execute)) {
            String[] split = execute.split("#");
            return SlidingWindow.builder()
                    .windowCount(Integer.parseInt(split[0]))
                    .windowStart(Long.parseLong(split[1]))
                    .windowEnd(Long.parseLong(split[2]))
                    .build();
        }
        return SlidingWindow.builder().build();
    }

    @Override
    public Map<String, Integer> slidingWindowCount(String keyPrefix) {
        DefaultRedisScript<String> actual = Singleton.get(EasyLogConstants.SLIDING_WINDOW_COUNT_LUA_PATH, () -> {
            DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(EasyLogConstants.SLIDING_WINDOW_COUNT_LUA_PATH)));
            redisScript.setResultType(String.class);
            return redisScript;
        });
        Map<String, Integer> windowCountMap = new HashMap<>();
        String execute = stringRedisTemplate.execute(actual, Collections.singletonList(keyPrefix));
        if (StringUtils.hasLength(execute)) {
            windowCountMap.putAll(Arrays.stream(execute.split(",")).collect(Collectors.toMap(
                    n -> n.split("#")[0].replace(keyPrefix, ""),
                    m -> Integer.parseInt(m.split("#")[1]))));
        }
        Arrays.asList("ERROR", "INFO", "WARN", "DEBUG", "TRACE").forEach(n -> windowCountMap.putIfAbsent(n, 0));
        return windowCountMap;
    }

    @Override
    public void addLogAlarmRule(LogAlarmRule logAlarmRule) {
        String appName = logAlarmRule.getAppName();
        String namespace = logAlarmRule.getNamespace();
        String loggerName = logAlarmRule.getLoggerName();
        String ruleId = SecureUtil.md5(appName + ":" + namespace + ":" + loggerName);
        logAlarmRule.setRuleId(ruleId);
        stringRedisTemplate.opsForValue().set(EasyLogConstants.LOG_ALARM_RULES + ruleId, JSONUtil.toJsonStr(logAlarmRule));
    }

    @Override
    public void addLogRealTimeFilterRule(LogRealTimeFilterRule logRealTimeFilterRule) {
        stringRedisTemplate.opsForValue().set(EasyLogConstants.LOG_REAL_TIME_FILTER_RULES + logRealTimeFilterRule.getClientId(), JSONUtil.toJsonStr(logRealTimeFilterRule));
    }

    @Override
    public String addAlarmPlatform(String alarmPlatformType, LogAlarmPlatform logAlarmPlatform) {
        String alarmPlatformId = NanoId.randomNanoId();
        stringRedisTemplate.opsForHash().put(EasyLogConstants.LOG_ALARM_PLATFORM + alarmPlatformType, alarmPlatformId, JSONUtil.toJsonStr(logAlarmPlatform));
        return alarmPlatformId;
    }

    @Override
    public LogAlarmPlatform getAlarmPlatform(String alarmPlatformType, String alarmPlatformId) {
        Object res = stringRedisTemplate.opsForHash().get(EasyLogConstants.LOG_ALARM_PLATFORM + alarmPlatformType, alarmPlatformId);
        return Objects.isNull(res) ? null : JSONUtil.toBean(res.toString(), LogAlarmPlatform.class);
    }

    @Override
    public void delAlarmPlatform(String alarmPlatformType, String alarmPlatformId) {
        stringRedisTemplate.opsForHash().delete(EasyLogConstants.LOG_ALARM_PLATFORM + alarmPlatformType, alarmPlatformId);
    }
}
