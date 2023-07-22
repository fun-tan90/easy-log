package com.chj.easy.log.admin.mqtt;

import cn.hutool.core.lang.Singleton;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.core.server.event.IMqttSessionListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledFuture;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/22 7:41
 */
@Slf4j
@Component
public class MqttSessionListener implements IMqttSessionListener {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    RedisService redisService;

    @Override
    public void onSubscribed(ChannelContext context, String clientId, String topicFilter, MqttQoS mqttQoS) {
        if (topicFilter.startsWith(EasyLogConstants.LOG_AFTER_FILTERED_TOPIC)) {
            redisService.addRealTimeFilterSubscribingClient(clientId);
        }
    }

    @Override
    public void onUnsubscribed(ChannelContext context, String clientId, String topicFilter) {
        if (topicFilter.startsWith(EasyLogConstants.LOG_AFTER_FILTERED_TOPIC)) {
            ScheduledFuture<?> scheduledFuture = Singleton.get(clientId, () -> null);
            if (!scheduledFuture.isCancelled()) {
                scheduledFuture.cancel(true);
            }
            redisService.delRealTimeFilterSubscribingClient(clientId);
            redisService.delLogRealTimeFilterRuleFromCache(clientId);
        }
    }
}
