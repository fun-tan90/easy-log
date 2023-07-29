package com.chj.easy.log.admin.mqtt.auth;

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.core.server.auth.IMqttServerPublishPermission;
import org.springframework.context.annotation.Configuration;
import org.tio.core.ChannelContext;


/**
 * description mqtt 服务端校验客户端是否有发布权限，请按照自己的需求和业务进行扩展
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/29 21:37
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class MqttPublishPermission implements IMqttServerPublishPermission {

	/**
	 * 否有发布权限
	 *
	 * @param context  ChannelContext
	 * @param clientId 客户端 id
	 * @param topic    topic
	 * @param qoS      MqttQoS
	 * @param isRetain 是否保留消息
	 * @return 否有发布权限
	 */
	@Override
	public boolean hasPermission(ChannelContext context, String clientId, String topic, MqttQoS qoS, boolean isRetain) {
		log.info("Mqtt client publish permission check clientId:{} topic:{}.", clientId, topic);
		// 可自定义业务，判断客户端是否有发布的权限。
		return true;
	}

}
