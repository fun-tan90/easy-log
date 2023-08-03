package com.chj.easy.log.event.listener;

import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.event.LogAlarmEvent;
import com.chj.easy.log.core.model.LogAlarmContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttPublishMessage;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.core.server.event.IMqttMessageListener;
import org.springframework.context.ApplicationContext;
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
@RequiredArgsConstructor
public class MqttServerMessageListener implements IMqttMessageListener {

    private final ApplicationContext applicationContext;

    @Override
    public void onMessage(ChannelContext context, String clientId, String topic, MqttQoS qoS, MqttPublishMessage message) {
        String msg = new String(message.getPayload(), StandardCharsets.UTF_8);
        log.info("clientId:{} message:{} payload:{}", clientId, message, msg);
        if (topic.equalsIgnoreCase(EasyLogConstants.LOG_ALARM_TOPIC)) {
            applicationContext.publishEvent(new LogAlarmEvent(this, JSONUtil.toBean(msg, LogAlarmContent.class)));
        }
    }
}
