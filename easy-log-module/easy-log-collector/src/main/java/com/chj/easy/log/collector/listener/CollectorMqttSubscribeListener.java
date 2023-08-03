package com.chj.easy.log.collector.listener;

import com.chj.easy.log.common.constant.EasyLogConstants;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.spring.client.MqttClientSubscribe;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/2 14:54
 */
@Slf4j
@Component
public class CollectorMqttSubscribeListener {

    @MqttClientSubscribe(value = EasyLogConstants.LOG_ALARM_RULES_TOPIC + "#", qos = MqttQoS.EXACTLY_ONCE)
    public void subLogAlarmRules(String topic, byte[] payload) {
        String msg = new String(payload, StandardCharsets.UTF_8);
        log.info("topic:{} payload:{}", topic, msg);
    }

    @MqttClientSubscribe(value = EasyLogConstants.LOG_REAL_TIME_FILTER_RULES_TOPIC + "#", qos = MqttQoS.EXACTLY_ONCE)
    public void subLogRealTimeFilterRule(String topic, byte[] payload) {
        String msg = new String(payload, StandardCharsets.UTF_8);
        log.info("topic:{} payload:{}", topic, msg);
    }

}