package com.chj.easy.log.compute.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.common.model.LogTransferred;
import com.chj.easy.log.common.threadpool.EasyLogThreadPool;
import com.chj.easy.log.compute.LogAlarmRulesManager;
import com.chj.easy.log.compute.LogRealTimeFilterRulesManager;
import com.chj.easy.log.core.model.LogAlarmContent;
import com.chj.easy.log.core.model.LogAlarmRule;
import com.chj.easy.log.core.model.LogRealTimeFilterRule;
import com.chj.easy.log.core.model.SlidingWindow;
import com.chj.easy.log.core.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.spring.client.MqttClientSubscribe;
import net.dreamlu.iot.mqtt.spring.client.MqttClientTemplate;
import org.jetlinks.reactor.ql.ReactorQL;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
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
 * @date 2023/8/2 14:54
 */
@Slf4j
@Component
public class ComputeMqttSubscribeListener {

    @Resource
    MqttClientTemplate mqttClientTemplate;

    @Resource
    CacheService cacheService;

    @MqttClientSubscribe(value = "$share/compute/" + EasyLogConstants.MQTT_LOG_PREFIX + "/#", qos = MqttQoS.AT_LEAST_ONCE)
    public void log(String topic, byte[] payload) {
        String msg = new String(payload, StandardCharsets.UTF_8);
        log.info("topic {} message {}", topic, msg);
        List<LogTransferred> logTransferredList = JSONUtil.toList(msg, LogTransferred.class);
        for (LogTransferred logTransferred : logTransferredList) {
            CompletableFuture<Void> cfAll = CompletableFuture.allOf(logInputSpeed(logTransferred, "recordId"), logAlarm(logTransferred, "recordId"), logRealTimeFilter(logTransferred));
            cfAll.join();
        }
    }

    /**
     * 日志收集速率
     */
    private CompletableFuture<Void> logInputSpeed(LogTransferred logTransferred, String recordId) {
        String level = logTransferred.getLevel();
        long timestamp = logTransferred.getTimestamp();
        return CompletableFuture.runAsync(() -> cacheService.slidingWindow(EasyLogConstants.S_W_LOG_INPUT_SPEED + level, recordId, timestamp, 5), EasyLogThreadPool.newEasyLogFixedPoolInstance());
    }

    /**
     * 日志告警
     *
     * @param logTransferred
     * @param recordId
     */
    private CompletableFuture<Void> logAlarm(LogTransferred logTransferred, String recordId) {
        return CompletableFuture.runAsync(() -> {
            String level = logTransferred.getLevel();
            if (!"error".equalsIgnoreCase(level)) {
                return;
            }
            String appName = logTransferred.getAppName();
            String namespace = logTransferred.getNamespace();
            String loggerName = logTransferred.getLoggerName();
            long timestamp = logTransferred.getTimestamp();

            Map<String, LogAlarmRule> cacheLogAlarmRuleMap = LogAlarmRulesManager.getLogAlarmRule(appName, namespace, "all", loggerName);
            if (CollectionUtils.isEmpty(cacheLogAlarmRuleMap)) {
                return;
            }
            cacheLogAlarmRuleMap.forEach((k, logAlarmRule) -> {
                Integer period = logAlarmRule.getPeriod();
                Integer threshold = logAlarmRule.getThreshold();
                SlidingWindow slidingWindow = cacheService.slidingWindow(EasyLogConstants.S_W_LOG_ALARM + appName + ":" + namespace + ":" + k, recordId, timestamp, period);
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
                    mqttClientTemplate.publish(EasyLogConstants.MQTT_LOG_ALARM_TOPIC, JSONUtil.toJsonStr(logAlarmContent).getBytes(StandardCharsets.UTF_8), MqttQoS.EXACTLY_ONCE);
                }
            });
        }, EasyLogThreadPool.newEasyLogFixedPoolInstance());
    }

    /**
     * 实时日志过滤
     *
     * @param logTransferred
     */
    private CompletableFuture<Void> logRealTimeFilter(LogTransferred logTransferred) {
        Map<String, Object> logStrMap = BeanUtil.beanToMap(logTransferred);
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
                    mqttClientTemplate.publish(EasyLogConstants.MQTT_LOG_AFTER_FILTERED_TOPIC + clientId, JSONUtil.toJsonStr(logStrMap).getBytes(StandardCharsets.UTF_8), MqttQoS.AT_LEAST_ONCE);
                }
            }
        }, EasyLogThreadPool.newEasyLogFixedPoolInstance());
    }

    @MqttClientSubscribe(value = EasyLogConstants.MQTT_LOG_ALARM_RULES_TOPIC + "#", qos = MqttQoS.EXACTLY_ONCE)
    public void subLogAlarmRules(String topic, byte[] payload) {
        String msg = new String(payload, StandardCharsets.UTF_8);
        log.info("topic:{} payload:{}", topic, msg);
        if (topic.endsWith("put")) {
            LogAlarmRulesManager.putLogAlarmRule(JSONUtil.toBean(msg, LogAlarmRule.class));
        } else if (topic.endsWith("remove")) {
            LogAlarmRulesManager.removeLogAlarmRule(JSONUtil.toBean(msg, LogAlarmRule.class));
        }
    }

    @MqttClientSubscribe(value = EasyLogConstants.LOG_REAL_TIME_FILTER_RULES_TOPIC + "#", qos = MqttQoS.EXACTLY_ONCE)
    public void subLogRealTimeFilterRule(String topic, byte[] payload) {
        String msg = new String(payload, StandardCharsets.UTF_8);
        log.info("topic:{} payload:{}", topic, msg);
        if (topic.endsWith("put")) {
            LogRealTimeFilterRulesManager.putLogRealTimeFilterRule(JSONUtil.toBean(msg, LogRealTimeFilterRule.class));
        } else if (topic.endsWith("remove")) {
            LogRealTimeFilterRulesManager.removeLogRealTimeFilterRule(JSONUtil.toBean(msg, LogRealTimeFilterRule.class));
        }
    }

}