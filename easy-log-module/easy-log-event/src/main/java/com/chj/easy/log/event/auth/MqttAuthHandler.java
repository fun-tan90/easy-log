package com.chj.easy.log.event.auth;

import cn.hutool.crypto.SecureUtil;
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
        if (clientId.startsWith(EasyLogConstants.MQTT_CLIENT_ID_SERVER_PREFIX)) {
            return true;
        }
        if (clientId.startsWith(EasyLogConstants.MQTT_CLIENT_ID_APP_PREFIX) && EasyLogConstants.MQTT_CLIENT_USERNAME.equals(userName) && EasyLogConstants.MQTT_CLIENT_PASSWORD.equals(password)) {
            return true;
        }
        if (clientId.startsWith(EasyLogConstants.MQTT_CLIENT_ID_FRONT_PREFIX)) {
            String md5 = SecureUtil.md5(clientId);
            String mqttUserName = md5.substring(0, EasyLogConstants.MQTT_MD5_SUB_INDEX);
            String mqttPassword = md5.substring(EasyLogConstants.MQTT_MD5_SUB_INDEX);
            return userName.equals(mqttUserName) && password.equals(mqttPassword);
        }
        return false;
    }

}
