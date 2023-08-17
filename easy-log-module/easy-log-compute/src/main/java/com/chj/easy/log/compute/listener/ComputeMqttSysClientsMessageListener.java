package com.chj.easy.log.compute.listener;

import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.compute.LogRealTimeFilterRulesManager;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttPublishMessage;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.core.client.IMqttClientMessageListener;
import net.dreamlu.iot.mqtt.spring.client.MqttClientSubscribe;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;

import java.nio.charset.StandardCharsets;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/12 14:24
 */
@Slf4j
@Component
@MqttClientSubscribe(value = "$SYS/brokers/+/clients/+/+", qos = MqttQoS.EXACTLY_ONCE)
public class ComputeMqttSysClientsMessageListener implements IMqttClientMessageListener {

    @Override
    public void onMessage(ChannelContext context, String topic, MqttPublishMessage message, byte[] payload) {
        String msg = new String(payload, StandardCharsets.UTF_8);
        log.debug("订阅到客户端上下线事件信息 topic:{} payload:{}", topic, msg);
        String clientId = JSONUtil.parseObj(msg).getStr("clientid");
        if (topic.endsWith("disconnected") && clientId.startsWith(EasyLogConstants.MQTT_CLIENT_ID_FRONT_PREFIX)) {
            LogRealTimeFilterRulesManager.removeLogRealTimeFilterRule(clientId);
        }
    }
}

