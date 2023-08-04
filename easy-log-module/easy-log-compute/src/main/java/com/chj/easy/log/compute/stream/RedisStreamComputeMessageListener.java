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
import org.jetlinks.reactor.ql.ReactorQL;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
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

//            ReactorQL.builder()
//                    .sql("select this from test where timeStamp >= {}") //按每秒分组,并计算流中数据平均值,如果平均值大于2则下游收到数据.
//                    .build()
//                    .start(Flux.just(entries))
//                    .subscribe(n -> {
//                                System.out.println(n);
//                            }
//                    );
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
            List<String> clientIds = LogRealTimeFilterRulesManager.stream().filter(n -> {
                ReactorQL reactorQl = LogRealTimeFilterRulesManager.getLogRealTimeFilterRule(n);
                if (Objects.isNull(reactorQl)) {
                    return true;
                }
                AtomicReference<Boolean> afterSqlFilter = new AtomicReference<>(false);
                reactorQl.start(Flux.just(logStrMap))
                        .subscribe(m -> afterSqlFilter.set(true),
                                err -> log.error("{}: {}", reactorQl.metadata().getSql().toString(), err.getMessage()));
                return afterSqlFilter.get();
            }).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(clientIds)) {
                for (String clientId : clientIds) {
                    mqttClientTemplate.publish(EasyLogConstants.LOG_AFTER_FILTERED_TOPIC + clientId, JSONUtil.toJsonStr(logStrMap).getBytes(StandardCharsets.UTF_8), MqttQoS.AT_LEAST_ONCE);
                }
            }
        }, EasyLogThreadPool.newEasyLogFixedPoolInstance());
    }
}
