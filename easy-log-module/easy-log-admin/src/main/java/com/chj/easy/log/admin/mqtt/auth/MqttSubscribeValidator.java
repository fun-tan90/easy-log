package com.chj.easy.log.admin.mqtt.auth;

import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.core.server.auth.IMqttServerSubscribeValidator;
import org.springframework.context.annotation.Configuration;
import org.tio.core.ChannelContext;


/**
 * description 订阅校验，请按照自己的需求和业务进行扩展
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/29 21:37
 */
@Configuration(proxyBeanMethods = false)
public class MqttSubscribeValidator implements IMqttServerSubscribeValidator {

    @Override
    public boolean isValid(ChannelContext context, String clientId, String topicFilter, MqttQoS qoS) {
        // 校验客户端订阅的 topic，校验成功返回 true，失败返回 false
        return true;
    }

}
