package com.chj.easy.log.compute.stream;

import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.common.threadpool.EasyLogThreadPool;
import com.chj.easy.log.compute.LogAlarmRulesManager;
import com.chj.easy.log.compute.LogRealTimeFilterRulesManager;
import com.chj.easy.log.core.model.LogAlarmContent;
import com.chj.easy.log.core.model.LogAlarmRule;
import com.chj.easy.log.core.model.SlidingWindow;
import com.chj.easy.log.core.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.spring.client.MqttClientTemplate;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/16 8:40
 */
@Slf4j(topic = EasyLogConstants.EASY_LOG_TOPIC)
@Component
public class RedisStreamComputeMessageListener implements StreamListener<String, MapRecord<String, String, byte[]>> {

    @Resource
    CacheService cacheService;

    @Resource
    MqttClientTemplate mqttClientTemplate;

    @Override
    public void onMessage(MapRecord<String, String, byte[]> entries) {
        if (entries != null) {
            String recordId = entries.getId().getValue();
            Map<String, byte[]> logMap = entries.getValue();
            CompletableFuture<Void> cfAll = CompletableFuture.allOf(logInputSpeed(logMap, recordId), logAlarm(logMap, recordId), logRealTimeFilter(logMap));
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
            String level = new String(logMap.get("level"));
            if (!"error".equalsIgnoreCase(level)) {
                return;
            }
            String appName = new String(logMap.get("appName"));
            String namespace = new String(logMap.get("namespace"));
            String loggerName = new String(logMap.get("loggerName"));
            String timeStamp = new String(logMap.get("timeStamp"));

            Map<String, LogAlarmRule> cacheLogAlarmRuleMap = LogAlarmRulesManager.getLogAlarmRule(appName, namespace, "all", loggerName);
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
                    LogAlarmContent logAlarmContent = LogAlarmContent
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
                            .build();
                    mqttClientTemplate.publish(EasyLogConstants.LOG_ALARM_TOPIC, JSONUtil.toJsonStr(logAlarmContent).getBytes(StandardCharsets.UTF_8), MqttQoS.EXACTLY_ONCE);
                }
            });
        }, EasyLogThreadPool.newEasyLogFixedPoolInstance());
    }

    /**
     * 实时日志过滤
     *
     * @param logMap
     */
    private CompletableFuture<Void> logRealTimeFilter(Map<String, byte[]> logMap) {
        Map<String, String> logStrMap = new HashMap<>(logMap.size());
        logMap.keySet().forEach(key -> logStrMap.put(key, new String(logMap.get(key))));
        return CompletableFuture.runAsync(() -> {
            List<String> clientIds = LogRealTimeFilterRulesManager.RULES_MAP.keySet().stream().filter(n -> {
                Map<String, String> realTimeFilterRules = LogRealTimeFilterRulesManager.RULES_MAP.get(n);
                for (String realTimeFilterRule : realTimeFilterRules.keySet()) {
                    String[] split = realTimeFilterRule.split("#");
                    String ruleKey = split[0];
                    String ruleWay = split[1];
                    String logVal = logStrMap.get(ruleKey);
                    String ruleVal = realTimeFilterRules.get(realTimeFilterRule);
                    if ("eq".equals(ruleWay)) {
                        if (!logVal.equals(ruleVal)) {
                            return false;
                        }
                    } else if ("should".equals(ruleWay)) {
                        List<String> list = Arrays.asList(ruleVal.split("%"));
                        Optional<String> any = list.stream().filter(logVal::contains).findAny();
                        if (!any.isPresent()) {
                            return false;
                        }
                    } else if ("gle".equals(ruleWay)) {
                        if (Long.parseLong(ruleVal) > Long.parseLong(logVal)) {
                            return false;
                        }
                    }
                }
                return true;
            }).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(clientIds)) {
                for (String clientId : clientIds) {
                    mqttClientTemplate.publish(EasyLogConstants.LOG_AFTER_FILTERED_TOPIC + clientId, JSONUtil.toJsonStr(logStrMap).getBytes(StandardCharsets.UTF_8), MqttQoS.AT_LEAST_ONCE);
                }
            }
        }, EasyLogThreadPool.newEasyLogFixedPoolInstance());
    }
}
