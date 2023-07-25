package com.chj.easy.log.compute.stream;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.model.LogAlarmContent;
import com.chj.easy.log.core.model.LogAlarmRule;
import com.chj.easy.log.core.model.SlidingWindow;
import com.chj.easy.log.core.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
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

    @Override
    public void onMessage(MapRecord<String, String, String> entries) {
        if (entries != null) {
            String recordId = entries.getId().getValue();
            Long timestamp = entries.getId().getTimestamp();
            Long sequence = entries.getId().getSequence();
            stringRedisTemplate.opsForStream().acknowledge(EasyLogConstants.REDIS_STREAM_KEY, EasyLogConstants.GROUP_COMPUTE_NAME, recordId);
            Map<String, String> logMap = entries.getValue();
            CompletableFuture<Void> cfAll = CompletableFuture.allOf(logInputSpeed(logMap, recordId), logAlarm(logMap, recordId), logRealTimeFilter(logMap, timestamp, sequence));
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
            String loggerName = logMap.get("loggerName");
            String timeStamp = logMap.get("timeStamp");

            List<Object> logAlarmRulesList = stringRedisTemplate.opsForHash().multiGet(EasyLogConstants.LOG_ALARM_RULES + appName + ":" + appEnv, Arrays.asList("all", loggerName));
            Map<String, LogAlarmRule> cacheLogAlarmRuleMap = logAlarmRulesList
                    .stream()
                    .filter(n -> !Objects.isNull(n))
                    .map(logAlarmRule -> JSONUtil.toBean(logAlarmRule.toString(), LogAlarmRule.class))
                    .filter(logAlarmRule -> "1".equals(logAlarmRule.getStatus()))
                    .collect(Collectors.toMap(LogAlarmRule::getLoggerName, Function.identity()));
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
                    redisService.addLogAlarm(LogAlarmContent
                            .builder()
                            .slidingWindow(slidingWindow)
                            .logAlarmRule(logAlarmRule)
                            .build()
                    );
                }
            });
        }, EasyLogManager.EASY_LOG_FIXED_THREAD_POOL);
    }

    /**
     * 实时日志过滤
     *
     * @param logMap
     */
    private CompletableFuture<Void> logRealTimeFilter(Map<String, String> logMap, Long timestamp, Long sequence) {
        return CompletableFuture.runAsync(() -> {
            Set<String> clientIds = redisService.getRealTimeFilterSubscribingClients();
            if (CollectionUtils.isEmpty(clientIds)) {
                return;
            }
            Iterator<String> clientIdsIterator = clientIds.iterator();
            while (clientIdsIterator.hasNext()) {
                String clientId = clientIdsIterator.next();
                JSONObject realTimeFilterRules = redisService.getLogRealTimeFilterRule(clientId);
                if (!realTimeFilterRules.isEmpty()) {
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
                for (String clientId : clientIds) {
                    redisService.addRealTimeFilteredLogs(clientId, logMap, timestamp + sequence);
                }
            }
        }, EasyLogManager.EASY_LOG_FIXED_THREAD_POOL);
    }
}
