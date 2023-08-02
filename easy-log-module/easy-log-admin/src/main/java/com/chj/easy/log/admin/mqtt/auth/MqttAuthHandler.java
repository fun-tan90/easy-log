package com.chj.easy.log.admin.mqtt.auth;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.core.server.auth.IMqttServerAuthHandler;
import org.springframework.context.annotation.Configuration;
import org.tio.core.ChannelContext;

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
        if (clientId.startsWith(EasyLogConstants.MQTT_CLIENT_ID_COMPUTE_PREFIX)) {
            return true;
        }
        if (clientId.startsWith(EasyLogConstants.MQTT_CLIENT_ID_APP_PREFIX) && EasyLogConstants.MQTT_CLIENT_USERNAME.equals(userName) && EasyLogConstants.MQTT_CLIENT_PASSWORD.equals(password)) {
            return true;
        }
        if (clientId.startsWith(EasyLogConstants.MQTT_CLIENT_ID_FRONT_PREFIX)) {
            String[] split = clientId.split(":");
            if (split.length != 2) {
                return false;
            }
            SaSession tokenSession = StpUtil.getTokenSessionByToken(split[1]);
            String mqttUserName = tokenSession.get("mqttUserName", () -> "");
            String mqttPassword = tokenSession.get("mqttPassword", () -> "");
            return userName.equals(mqttUserName) && password.equals(mqttPassword);
        }
        return false;
    }

}
