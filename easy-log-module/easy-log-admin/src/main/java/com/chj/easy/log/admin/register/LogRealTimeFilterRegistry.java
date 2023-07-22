package com.chj.easy.log.admin.register;

import cn.hutool.core.map.SafeConcurrentHashMap;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.spring.server.MqttServerTemplate;
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
public class LogRealTimeFilterRegistry {

    private static final SafeConcurrentHashMap<String, ScheduledFuture<?>> SCHEDULED_FUTURE_POOL = new SafeConcurrentHashMap<>();

    @Resource
    RedisService redisService;

    @Resource
    MqttServerTemplate mqttServerTemplate;

    public void register(String clientId, Map<String, String> realTimeFilterRules) {
        redisService.addLogRealTimeFilterRule(clientId, realTimeFilterRules);
        ScheduledFuture<?> scheduledFuture = EasyLogManager.EASY_LOG_SCHEDULED_EXECUTOR.scheduleWithFixedDelay(() -> {
            List<String> filteredLogs = redisService.popRealTimeFilteredLog(clientId);
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

    public void unRegister(String clientId) {
        ScheduledFuture<?> scheduledFuture = SCHEDULED_FUTURE_POOL.get(clientId);
        if (!Objects.isNull(scheduledFuture) && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(true);
        }
        redisService.delRealTimeFilterSubscribingClient(clientId);
        redisService.delLogRealTimeFilterRule(clientId);
    }

}
