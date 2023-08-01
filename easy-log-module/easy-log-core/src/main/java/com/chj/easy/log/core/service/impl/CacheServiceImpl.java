package com.chj.easy.log.core.service.impl;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.lang.id.NanoId;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.model.LogAlarmContent;
import com.chj.easy.log.core.model.LogAlarmPlatform;
import com.chj.easy.log.core.model.LogAlarmRule;
import com.chj.easy.log.core.model.SlidingWindow;
import com.chj.easy.log.core.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
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

    @Resource
    private StreamMessageListenerContainer<String, MapRecord<String, String, byte[]>> streamMessageListenerContainer;

    @Override
    public void initGroupAndConsumers(String streamKey, String groupName, String consumerNamePrefix, int[] consumerGlobalOrders, StreamListener<String, MapRecord<String, String, byte[]>> streamListener) {
        Boolean hasKey = stringRedisTemplate.hasKey(streamKey);
        if (Boolean.FALSE.equals(hasKey)) {
            stringRedisTemplate.opsForStream().createGroup(streamKey, groupName);
        }
        StreamInfo.XInfoGroups groups = stringRedisTemplate.opsForStream().groups(streamKey);
        Optional<StreamInfo.XInfoGroup> xInfoGroupOpt = groups.stream().filter(n -> n.groupName().equals(groupName)).findAny();
        if (!xInfoGroupOpt.isPresent()) {
            stringRedisTemplate.opsForStream().createGroup(streamKey, groupName);
        }
        for (int consumerGlobalOrder : consumerGlobalOrders) {
            String consumerName = consumerNamePrefix + consumerGlobalOrder;
            streamMessageListenerContainer
                    .receive(
                            Consumer.from(groupName, consumerName),
                            StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
                            streamListener
                    );
        }
    }

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
        String execute = stringRedisTemplate.execute(actual, Collections.singletonList(keyPrefix));
        if (StringUtils.hasLength(execute)) {
            return Arrays.stream(execute.split(",")).collect(Collectors.toMap(
                    n -> n.split("#")[0].replace(keyPrefix, ""),
                    m -> Integer.parseInt(m.split("#")[1])));
        }
        return new HashMap<>();
    }

    @Override
    public void addLogRealTimeFilterRule(String mqttClientId, Map<String, String> realTimeFilterRules) {
        stringRedisTemplate.opsForValue().set(EasyLogConstants.REAL_TIME_FILTER_RULES + mqttClientId, JSONUtil.toJsonStr(realTimeFilterRules));
    }

    @Override
    public void delLogRealTimeFilterRule(String mqttClientId) {
        stringRedisTemplate.delete(EasyLogConstants.REAL_TIME_FILTER_RULES + mqttClientId);
    }

    @Override
    public Map<String, String> getLogRealTimeFilterRule(String mqttClientId) {
        String realTimeFilterRulesStr = stringRedisTemplate.opsForValue().get(EasyLogConstants.REAL_TIME_FILTER_RULES + mqttClientId);
        JSONObject entries = Optional.ofNullable(realTimeFilterRulesStr).map(JSONUtil::parseObj).orElse(new JSONObject());
        Map<String, String> realTimeFilterRules = new HashMap<>();
        for (String key : entries.keySet()) {
            realTimeFilterRules.put(key, entries.getStr(key));
        }
        return realTimeFilterRules;
    }

    @Override
    public void addRealTimeFilterSubscribingClient(String mqttClientId) {
        stringRedisTemplate.opsForSet().add(EasyLogConstants.REAL_TIME_FILTER_SUBSCRIBING_CLIENTS, mqttClientId);
    }

    @Override
    public void delRealTimeFilterSubscribingClient(String mqttClientId) {
        stringRedisTemplate.opsForSet().remove(EasyLogConstants.REAL_TIME_FILTER_SUBSCRIBING_CLIENTS, mqttClientId);
    }

    @Override
    public Set<String> getRealTimeFilterSubscribingClients() {
        return stringRedisTemplate.opsForSet().members(EasyLogConstants.REAL_TIME_FILTER_SUBSCRIBING_CLIENTS);
    }

    @Override
    public void addRealTimeFilteredLogs(String clientId, Map<String, byte[]> logMap, double score) {
        stringRedisTemplate.opsForZSet().add(EasyLogConstants.REAL_TIME_FILTER_Z_SET + clientId, JSONUtil.toJsonStr(logMap), score);
    }

    @Override
    public List<String> popRealTimeFilteredLog(String clientId) {
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().popMax(EasyLogConstants.REAL_TIME_FILTER_Z_SET + clientId, -1);
        if (CollectionUtils.isEmpty(typedTuples)) {
            return new ArrayList<>();
        }
        return typedTuples.stream().sorted(Comparator.comparing(ZSetOperations.TypedTuple::getScore)).map(ZSetOperations.TypedTuple::getValue).collect(Collectors.toList());
    }

    @Override
    public void addLogAlarmContent(LogAlarmContent logAlarmContent) {
        String ruleId = logAlarmContent.getRuleId();
        Integer period = logAlarmContent.getPeriod();
        Boolean res = stringRedisTemplate.opsForValue().setIfAbsent(EasyLogConstants.LOG_ALARM_LOCK + ruleId, "", period, TimeUnit.SECONDS);
        if (Boolean.TRUE.equals(res)) {
            stringRedisTemplate.opsForList().leftPush(EasyLogConstants.LOG_ALARM, JSONUtil.toJsonStr(logAlarmContent));
        }
    }

    @Override
    public LogAlarmContent popLogAlarmContent(long timeout) {
        String rightPop = stringRedisTemplate.opsForList().rightPop(EasyLogConstants.LOG_ALARM, timeout, TimeUnit.SECONDS);
        if (StringUtils.hasLength(rightPop)) {
            return JSONUtil.toBean(rightPop, LogAlarmContent.class);
        }
        return null;
    }

    @Override
    public String addLogAlarmRule(LogAlarmRule logAlarmRule) {
        String appName = logAlarmRule.getAppName();
        String namespace = logAlarmRule.getNamespace();
        String ruleKey = EasyLogConstants.LOG_ALARM_RULES + appName + ":" + namespace;
        String loggerName = logAlarmRule.getLoggerName();
        String ruleId = SecureUtil.md5(ruleKey + ":" + loggerName);
        logAlarmRule.setRuleId(ruleId);
        stringRedisTemplate.opsForHash().put(ruleKey, loggerName, JSONUtil.toJsonStr(logAlarmRule));
        return ruleId;
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
