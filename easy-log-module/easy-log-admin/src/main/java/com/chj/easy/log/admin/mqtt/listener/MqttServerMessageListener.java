package com.chj.easy.log.admin.mqtt.listener;

import net.dreamlu.iot.mqtt.codec.MqttPublishMessage;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.core.server.event.IMqttMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tio.core.ChannelContext;

import java.nio.charset.StandardCharsets;


/**
 * description 消息监听
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/29 21:35
 */
@Service
public class MqttServerMessageListener implements IMqttMessageListener {
	private static final Logger logger = LoggerFactory.getLogger(MqttServerMessageListener.class);

	@Override
	public void onMessage(ChannelContext context, String clientId, String topic, MqttQoS qoS, MqttPublishMessage message) {
		logger.info("clientId:{} message:{} payload:{}", clientId, message, new String(message.getPayload(), StandardCharsets.UTF_8));
	}
}
