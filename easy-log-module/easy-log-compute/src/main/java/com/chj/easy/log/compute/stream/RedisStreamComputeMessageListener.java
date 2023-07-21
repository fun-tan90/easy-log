package com.chj.easy.log.compute.stream;

import cn.hutool.core.lang.Singleton;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.event.LogAlarmEvent;
import com.chj.easy.log.core.model.LogAlarmRule;
import com.chj.easy.log.core.model.SlidingWindow;
import com.chj.easy.log.core.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.spring.server.MqttServerTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/16 8:40
 */
@Slf4j
@Component
public class RedisStreamComputeMessageListener implements StreamListener<String, MapRecord<String, String, String>> {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    RedisService redisService;

    @Resource
    MqttServerTemplate mqttServerTemplate;

    @Resource
    ApplicationContext applicationContext;

    @Override
    public void onMessage(MapRecord<String, String, String> entries) {
        if (entries != null) {
            String recordId = entries.getId().getValue();
            stringRedisTemplate.opsForStream().acknowledge(EasyLogConstants.REDIS_STREAM_KEY, EasyLogConstants.GROUP_COMPUTE_NAME, recordId);
            Map<String, String> logMap = entries.getValue();
            CompletableFuture<Void> cfAll = CompletableFuture.allOf(logInputSpeed(logMap, recordId), logAlarm(logMap, recordId), logRealTimeFilter(logMap));
            cfAll.join();
        }
    }

    /**
     * 日志收集速率
     */
    private CompletableFuture<Void> logInputSpeed(Map<String, String> logMap, String recordId) {
        String level = logMap.get("level");
        String timeStamp = logMap.get("timeStamp");
        return CompletableFuture.runAsync(() -> redisService.slidingWindow(EasyLogConstants.S_W_LOG_INPUT_SPEED + level, recordId, Long.parseLong(timeStamp), 5), EasyLogManager.EASY_LOG_FIXED_THREAD_POOL);
    }

    /**
     * 日志告警
     *
     * @param logMap
     */
    private CompletableFuture<Void> logAlarm(Map<String, String> logMap, String recordId) {
        return CompletableFuture.runAsync(() -> {
            // TODO
            String level = logMap.get("level");
            String appName = logMap.get("appName");
            String appEnv = logMap.get("appEnv");
            String timeStamp = logMap.get("timeStamp");

            Map<String, LogAlarmRule> cacheLogAlarmRuleMap = Singleton.get(EasyLogConstants.LOG_ALARM_RULES + appName + ":" + appEnv, () -> {
                Set<String> logAlarmRuleKeys = stringRedisTemplate.keys(EasyLogConstants.LOG_ALARM_RULES + appName + ":" + appEnv + ":*");
                if (CollectionUtils.isEmpty(logAlarmRuleKeys)) {
                    return new HashMap<>();
                }
                return logAlarmRuleKeys.stream()
                        .map(logAlarmRuleKey -> JSONUtil.toBean(stringRedisTemplate.opsForValue().get(logAlarmRuleKey), LogAlarmRule.class))
                        .filter(logAlarmRule -> "1".equals(logAlarmRule.getStatus()))
                        .collect(Collectors.toMap(LogAlarmRule::getLoggerName, Function.identity()));
            });
            if (CollectionUtils.isEmpty(cacheLogAlarmRuleMap)) {
                return;
            }
            cacheLogAlarmRuleMap.forEach((k, logAlarmRule) -> {
                Integer period = logAlarmRule.getPeriod();
                Integer threshold = logAlarmRule.getThreshold();
                SlidingWindow slidingWindow = redisService.slidingWindow(EasyLogConstants.S_W_LOG_ALARM + appName + ":" + appEnv + ":" + k, recordId, Long.parseLong(timeStamp), period);
                Integer windowCount = slidingWindow.getWindowCount();
                log.info("阈值大小:{},滑动窗口内计数大小:{}", threshold, windowCount);
                if (windowCount == threshold + 1) {
                    applicationContext.publishEvent(new LogAlarmEvent(this, slidingWindow.getWindowStart(), slidingWindow.getWindowEnd(), windowCount, logAlarmRule));
                }
            });
        }, EasyLogManager.EASY_LOG_FIXED_THREAD_POOL);
    }

    /**
     * 实时日志过滤
     *
     * @param logMap
     */
    private CompletableFuture<Void> logRealTimeFilter(Map<String, String> logMap) {
        return CompletableFuture.runAsync(() -> {
            Set<String> clientIds = stringRedisTemplate.opsForSet().members(EasyLogConstants.MQTT_ONLINE_CLIENTS);
            if (CollectionUtils.isEmpty(clientIds)) {
                return;
            }
            Iterator<String> clientIdsIterator = clientIds.iterator();
            while (clientIdsIterator.hasNext()) {
                String clientId = clientIdsIterator.next();
                String realTimeFilterRulesStr = stringRedisTemplate.opsForValue().get(EasyLogConstants.REAL_TIME_FILTER_RULES + clientId);
                if (StringUtils.hasLength(realTimeFilterRulesStr)) {
                    JSONObject realTimeFilterRules = JSONUtil.parseObj(realTimeFilterRulesStr);
                    for (String realTimeFilterRule : realTimeFilterRules.keySet()) {
                        String[] split = realTimeFilterRule.split("#");
                        String ruleKey = split[0];
                        String ruleWay = split[1];
                        String logVal = logMap.get(ruleKey);
                        String ruleVal = realTimeFilterRules.getStr(realTimeFilterRule);
                        if ("eq".equals(ruleWay)) {
                            if (!logVal.equals(ruleVal)) {
                                clientIdsIterator.remove();
                                break;
                            }
                        } else if ("should".equals(ruleWay)) {
                            List<String> list = Arrays.asList(ruleVal.split("%"));
                            Optional<String> any = list.stream().filter(logVal::contains).findAny();
                            if (!any.isPresent()) {
                                clientIdsIterator.remove();
                                break;
                            }
                        } else if ("gle".equals(ruleWay)) {
                            if (Long.parseLong(ruleVal) > Long.parseLong(logVal)) {
                                clientIdsIterator.remove();
                                break;
                            }
                        }
                    }
                } else {
                    clientIdsIterator.remove();
                }
            }
            if (!CollectionUtils.isEmpty(clientIds)) {
                log.info("日志过滤已完成，待发送客户端个数{}", clientIds.size());
                byte[] logBytes = JSONUtil.toJsonStr(logMap).getBytes(StandardCharsets.UTF_8);
                for (String clientId : clientIds) {
                    mqttServerTemplate.publish(clientId, EasyLogConstants.LOG_REAL_TIME_FILTERED_TOPIC, logBytes, MqttQoS.AT_MOST_ONCE);
                }
            }
        }, EasyLogManager.EASY_LOG_FIXED_THREAD_POOL);
    }
}
