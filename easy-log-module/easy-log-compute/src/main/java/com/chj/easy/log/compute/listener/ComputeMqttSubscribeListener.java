package com.chj.easy.log.compute.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.common.model.LogTransferred;
import com.chj.easy.log.common.threadpool.EasyLogThreadPool;
import com.chj.easy.log.compute.LogAlarmRulesManager;
import com.chj.easy.log.compute.LogRealTimeFilterRulesManager;
import com.chj.easy.log.core.model.*;
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
import java.util.Date;
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

    @MqttClientSubscribe(value = "$share/compute/" + EasyLogConstants.MQTT_LOG_PREFIX, qos = MqttQoS.AT_LEAST_ONCE)
    public void log(String topic, byte[] payload) {
        String msg = new String(payload, StandardCharsets.UTF_8);
        List<LogTransferred> logTransferreds = JSONUtil.toList(msg, LogTransferred.class);
        for (LogTransferred logTransferred : logTransferreds) {
            LogDoc logDoc = LogDoc.builder()
                    .id("")
                    .timestamp(DateUtil.format(new Date(logTransferred.getTimeStamp()), DatePattern.NORM_DATETIME_MS_PATTERN))
                    .appName(logTransferred.getAppName())
                    .namespace(logTransferred.getNamespace())
                    .level(logTransferred.getLevel())
                    .loggerName(logTransferred.getLoggerName())
                    .threadName(logTransferred.getThreadName())
                    .traceId(logTransferred.getTraceId())
                    .spanId(logTransferred.getSpanId())
                    .currIp(logTransferred.getCurrIp())
                    .preIp(logTransferred.getPreIp())
                    .method(logTransferred.getMethod())
                    .lineNumber(logTransferred.getLineNumber())
                    .content(logTransferred.getContent())
                    .mdc(logTransferred.getMdc())
                    .build();
            CompletableFuture<Void> cfAll = CompletableFuture.allOf(logInputSpeed(logDoc, "recordId"), logAlarm(logDoc, "recordId"), logRealTimeFilter(logDoc));
            cfAll.join();
        }
    }

    /**
     * 日志收集速率
     */
    private CompletableFuture<Void> logInputSpeed(LogDoc logDoc, String recordId) {
        String level = logDoc.getLevel();
        String timeStamp = logDoc.getTimestamp();
        return CompletableFuture.runAsync(() -> cacheService.slidingWindow(EasyLogConstants.S_W_LOG_INPUT_SPEED + level, recordId, Long.parseLong(timeStamp), 5), EasyLogThreadPool.newEasyLogFixedPoolInstance());
    }

    /**
     * 日志告警
     *
     * @param logDoc
     */
    private CompletableFuture<Void> logAlarm(LogDoc logDoc, String recordId) {
        return CompletableFuture.runAsync(() -> {
            String level = logDoc.getLevel();
            if (!"error".equalsIgnoreCase(level)) {
                return;
            }
            String appName = logDoc.getAppName();
            String namespace = logDoc.getNamespace();
            String loggerName = logDoc.getLoggerName();
            String timeStamp = logDoc.getTimestamp();

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
                    mqttClientTemplate.publish(EasyLogConstants.MQTT_LOG_ALARM_TOPIC, JSONUtil.toJsonStr(logAlarmContent).getBytes(StandardCharsets.UTF_8), MqttQoS.EXACTLY_ONCE);
                }
            });
        }, EasyLogThreadPool.newEasyLogFixedPoolInstance());
    }

    /**
     * 实时日志过滤
     *
     * @param logDoc
     */
    private CompletableFuture<Void> logRealTimeFilter(LogDoc logDoc) {
        Map<String, Object> logStrMap = BeanUtil.beanToMap(logDoc);
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