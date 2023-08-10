package com.chj.easy.log.mqtt.listener;

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.core.server.event.IMqttSessionListener;
import org.tio.core.ChannelContext;

/**
 * description 监听订阅
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/22 7:41
 */
@Slf4j
public class MqttSessionListener implements IMqttSessionListener {

    @Override
    public void onSubscribed(ChannelContext context, String clientId, String topicFilter, MqttQoS mqttQoS) {
        log.info("onSubscribed clientId: {} topic: {} qos: {}", clientId, topicFilter, mqttQoS.value());
    }

    @Override
    public void onUnsubscribed(ChannelContext context, String clientId, String topicFilter) {
        log.info("onUnsubscribed clientId: {} topic: {}", clientId, topicFilter);
    }
}
