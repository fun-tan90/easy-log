package com.chj.easy.log.admin.mqtt;

import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.event.LogAlarmUnRegisterEvent;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.core.server.event.IMqttConnectStatusListener;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;

import javax.annotation.Resource;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/18 16:55
 */
@Slf4j(topic = EasyLogConstants.EASY_LOG_TOPIC)
@Component
public class MqttConnectStatusListener implements IMqttConnectStatusListener {

    @Resource
    ApplicationContext applicationContext;

    @Override
    public void online(ChannelContext context, String clientId, String username) {
        log.info("{} is online", clientId);
    }

    @Override
    public void offline(ChannelContext context, String clientId, String username, String reason) {
        log.info("{} is offline", clientId);
        if (clientId.startsWith(EasyLogConstants.MQTT_CLIENT_ID_FRONT_PREFIX)) {
            applicationContext.publishEvent(new LogAlarmUnRegisterEvent(this, clientId));
        } else if (clientId.startsWith(EasyLogConstants.MQTT_CLIENT_ID_APP_PREFIX)) {

        }
    }
}
