package com.chj.easy.log.admin.mqtt.auth;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.core.server.auth.IMqttServerAuthHandler;
import org.springframework.context.annotation.Configuration;
import org.tio.core.ChannelContext;
import org.tio.core.Node;

/**
 * description mqtt tcp、websocket 认证，请按照自己的需求和业务进行扩展
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/29 21:38
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class MqttAuthHandler implements IMqttServerAuthHandler {

	@Override
	public boolean authenticate(ChannelContext context, String uniqueId, String clientId, String userName, String password) {
		// 获取客户端信息
		Node clientNode = context.getClientNode();
		log.info(JSONUtil.toJsonStr(clientNode));
		// 客户端认证逻辑实现
		if (clientId.startsWith(EasyLogConstants.MQTT_CLIENT_ID_APP_PREFIX) && EasyLogConstants.MQTT_CLIENT_USERNAME.equals(userName) && EasyLogConstants.MQTT_CLIENT_PASSWORD.equals(password)) {
			return true;
		}
		String tokenValue = clientId.split(":")[1];
		SaSession tokenSession = StpUtil.getTokenSessionByToken(tokenValue);
		String mqttUserName = tokenSession.get("mqttUserName", () -> "");
		String mqttPassword = tokenSession.get("mqttPassword", () -> "");
		return userName.equals(mqttUserName) && password.equals(mqttPassword);
	}

}
