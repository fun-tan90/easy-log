package com.chj.easy.log.compute.stream;

import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.common.threadpool.EasyLogThreadPool;
import com.chj.easy.log.core.model.LogAlarmContent;
import com.chj.easy.log.core.model.LogAlarmRule;
import com.chj.easy.log.core.model.SlidingWindow;
import com.chj.easy.log.core.service.CacheService;
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
public class RedisStreamComputeMessageListener implements StreamListener<String, MapRecord<String, String, byte[]>> {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    CacheService cacheService;

    @Override
    public void onMessage(MapRecord<String, String, byte[]> entries) {
        if (entries != null) {
            String recordId = entries.getId().getValue();
            Long timestamp = entries.getId().getTimestamp();
            Long sequence = entries.getId().getSequence();
            stringRedisTemplate.opsForStream().acknowledge(EasyLogConstants.REDIS_STREAM_KEY, EasyLogConstants.GROUP_COMPUTE_NAME, recordId);
            Map<String, byte[]> logMap = entries.getValue();
            CompletableFuture<Void> cfAll = CompletableFuture.allOf(logInputSpeed(logMap, recordId), logAlarm(logMap, recordId), logRealTimeFilter(logMap, timestamp, sequence));
            cfAll.join();
        }
    }

    /**
     * 日志收集速率
     */
    private CompletableFuture<Void> logInputSpeed(Map<String, byte[]> logMap, String recordId) {
        String level = new String(logMap.get("level"));
        String timeStamp = new String(logMap.get("timeStamp"));
        return CompletableFuture.runAsync(() -> cacheService.slidingWindow(EasyLogConstants.S_W_LOG_INPUT_SPEED + level, recordId, Long.parseLong(timeStamp), 5), EasyLogThreadPool.newEasyLogFixedPoolInstance());
    }

    /**
     * 日志告警
     *
     * @param logMap
     */
    private CompletableFuture<Void> logAlarm(Map<String, byte[]> logMap, String recordId) {
        return CompletableFuture.runAsync(() -> {
            // TODO
            String level = new String(logMap.get("level"));
            String appName = new String(logMap.get("appName"));
            String namespace = new String(logMap.get("namespace"));
            String loggerName = new String(logMap.get("loggerName"));
            String timeStamp = new String(logMap.get("timeStamp"));

            List<Object> logAlarmRulesList = stringRedisTemplate.opsForHash().multiGet(EasyLogConstants.LOG_ALARM_RULES + appName + ":" + namespace, Arrays.asList("all", loggerName));
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
                SlidingWindow slidingWindow = cacheService.slidingWindow(EasyLogConstants.S_W_LOG_ALARM + appName + ":" + namespace + ":" + k, recordId, Long.parseLong(timeStamp), period);
                Integer windowCount = slidingWindow.getWindowCount();
                log.info("阈值大小:{},滑动窗口内计数大小:{}", threshold, windowCount);
                if (windowCount == threshold + 1) {
                    cacheService.addLogAlarmContent(
                            LogAlarmContent
                                    .builder()
                                    .alarmPlatformType(logAlarmRule.getAlarmPlatformType())
                                    .alarmPlatformId(logAlarmRule.getAlarmPlatformId())
                                    .windowStart(slidingWindow.getWindowStart())
                                    .windowEnd(slidingWindow.getWindowEnd())
                                    .ruleId(logAlarmRule.getRuleId())
                                    .appName(logAlarmRule.getAppName())
                                    .namespace(logAlarmRule.getNamespace())
                                    .loggerName(k)
                                    .receiverList(logAlarmRule.getReceiverList())
                                    .threshold(logAlarmRule.getThreshold())
                                    .period(logAlarmRule.getPeriod())
                                    .build()
                    );
                }
            });
        }, EasyLogThreadPool.newEasyLogFixedPoolInstance());
    }

    /**
     * 实时日志过滤
     *
     * @param logMap
     */
    private CompletableFuture<Void> logRealTimeFilter(Map<String, byte[]> logMap, Long timestamp, Long sequence) {
        return CompletableFuture.runAsync(() -> {
            Set<String> clientIds = cacheService.getRealTimeFilterSubscribingClients();
            if (CollectionUtils.isEmpty(clientIds)) {
                return;
            }
            Iterator<String> clientIdsIterator = clientIds.iterator();
            while (clientIdsIterator.hasNext()) {
                String clientId = clientIdsIterator.next();
                Map<String, String> realTimeFilterRules = cacheService.getLogRealTimeFilterRule(clientId);
                if (!realTimeFilterRules.isEmpty()) {
                    for (String realTimeFilterRule : realTimeFilterRules.keySet()) {
                        String[] split = realTimeFilterRule.split("#");
                        String ruleKey = split[0];
                        String ruleWay = split[1];
                        String logVal = new String(logMap.get(ruleKey));
                        String ruleVal = realTimeFilterRules.get(realTimeFilterRule);
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
                    cacheService.addRealTimeFilteredLogs(clientId, logMap, timestamp + sequence);
                }
            }
        }, EasyLogThreadPool.newEasyLogFixedPoolInstance());
    }
}
