package com.chj.easy.log.admin.handler;

import cn.hutool.core.map.SafeConcurrentHashMap;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.event.LogAlarmRegisterEvent;
import com.chj.easy.log.core.event.LogAlarmUnRegisterEvent;
import com.chj.easy.log.core.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.spring.server.MqttServerTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/22 10:25
 */
@Slf4j
@Component
public class LogRealTimeFilterEventHandler {

    private static final SafeConcurrentHashMap<String, ScheduledFuture<?>> SCHEDULED_FUTURE_POOL = new SafeConcurrentHashMap<>();

    @Resource
    CacheService cacheService;

    @Resource
    MqttServerTemplate mqttServerTemplate;

    @EventListener
    public void logAlarmRegisterEvent(LogAlarmRegisterEvent logAlarmRegisterEvent) {
        String clientId = logAlarmRegisterEvent.getClientId();
        Map<String, String> realTimeFilterRules = logAlarmRegisterEvent.getRealTimeFilterRules();
        cacheService.addLogRealTimeFilterRule(clientId, realTimeFilterRules);
        ScheduledFuture<?> scheduledFuture = EasyLogManager.EASY_LOG_SCHEDULED_EXECUTOR.scheduleWithFixedDelay(() -> {
            List<String> filteredLogs = cacheService.popRealTimeFilteredLog(clientId);
            log.info("推送过滤后的日志，条数为{}", filteredLogs.size());
            if (CollectionUtils.isEmpty(filteredLogs)) {
                return;
            }
            for (String filteredLog : filteredLogs) {
                mqttServerTemplate.publish(clientId, EasyLogConstants.LOG_AFTER_FILTERED_TOPIC, filteredLog.getBytes(StandardCharsets.UTF_8), MqttQoS.AT_LEAST_ONCE);
            }
        }, 1, 50, TimeUnit.MILLISECONDS);
        SCHEDULED_FUTURE_POOL.put(clientId, scheduledFuture);
    }

    @EventListener
    public void logAlarmUnRegisterEvent(LogAlarmUnRegisterEvent logAlarmUnRegisterEvent) {
        String clientId = logAlarmUnRegisterEvent.getClientId();
        ScheduledFuture<?> scheduledFuture = SCHEDULED_FUTURE_POOL.remove(clientId);
        if (!Objects.isNull(scheduledFuture) && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(true);
        }
        cacheService.delRealTimeFilterSubscribingClient(clientId);
        cacheService.delLogRealTimeFilterRule(clientId);
    }
}
