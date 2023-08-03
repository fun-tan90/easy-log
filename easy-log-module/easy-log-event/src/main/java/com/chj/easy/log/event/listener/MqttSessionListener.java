package com.chj.easy.log.event.listener;

import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.event.LogRealTimeFilterUnRegisterEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.core.server.event.IMqttSessionListener;
import org.springframework.context.ApplicationContext;
import org.tio.core.ChannelContext;

/**
 * description 监听订阅
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/22 7:41
 */
@Slf4j
@RequiredArgsConstructor
public class MqttSessionListener implements IMqttSessionListener {

    private final ApplicationContext applicationContext;

    @Override
    public void onSubscribed(ChannelContext context, String clientId, String topicFilter, MqttQoS mqttQoS) {
    }

    @Override
    public void onUnsubscribed(ChannelContext context, String clientId, String topicFilter) {
        if (topicFilter.startsWith(EasyLogConstants.LOG_AFTER_FILTERED_TOPIC)) {
            applicationContext.publishEvent(new LogRealTimeFilterUnRegisterEvent(this, clientId));
        }
    }
}
