package com.chj.easy.log.admin.mqtt.listener;

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttPublishMessage;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.core.server.event.IMqttMessageListener;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;

import java.nio.charset.StandardCharsets;


/**
 * description 消息监听
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/29 21:35
 */
@Slf4j
@Component
public class MqttServerMessageListener implements IMqttMessageListener {

    @Override
    public void onMessage(ChannelContext context, String clientId, String topic, MqttQoS qoS, MqttPublishMessage message) {
        log.info("clientId:{} message:{} payload:{}", clientId, message, new String(message.getPayload(), StandardCharsets.UTF_8));
    }
}
