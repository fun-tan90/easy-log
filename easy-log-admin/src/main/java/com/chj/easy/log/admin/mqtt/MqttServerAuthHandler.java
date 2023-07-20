package com.chj.easy.log.admin.mqtt;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.core.server.auth.IMqttServerAuthHandler;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/20 14:27
 */
@Slf4j
@Component
public class MqttServerAuthHandler implements IMqttServerAuthHandler {
    @Override
    public boolean authenticate(ChannelContext context, String uniqueId, String clientId, String userName, String password) {
        SaSession tokenSession = StpUtil.getTokenSessionByToken(clientId);
        String mqttUserName = tokenSession.get("mqttUserName", () -> null);
        String mqttPassword = tokenSession.get("mqttPassword", () -> null);
        return userName.equals(mqttUserName) && password.equals(mqttPassword);
    }
}
