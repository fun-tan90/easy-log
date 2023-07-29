package com.chj.easy.log.admin.mqtt;

import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttPublishMessage;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.core.server.event.IMqttMessageListener;
import net.dreamlu.iot.mqtt.spring.server.MqttServerTemplate;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.tio.core.ChannelContext;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/29 10:43
 */
@Slf4j
@Service
public class MqttServerMessageListener implements IMqttMessageListener, SmartInitializingSingleton {
    @Resource
    private ApplicationContext applicationContext;

    private MqttServerTemplate mqttServerTemplate;

    @Override
    public void onMessage(ChannelContext context, String clientId, String topic, MqttQoS qos, MqttPublishMessage message) {
        log.info("context:{} clientId:{} message:{} payload:{}", context, clientId, message, new String(message.payload(), StandardCharsets.UTF_8));
    }

    @Override
    public void afterSingletonsInstantiated() {
        // 单利 bean 初始化完成之后从 ApplicationContext 中获取 bean
        mqttServerTemplate = applicationContext.getBean(MqttServerTemplate.class);
    }
}
