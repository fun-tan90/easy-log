package fun.tan90.easy.log.admin.listener;

import cn.hutool.json.JSONUtil;
import fun.tan90.easy.log.admin.service.LogAlarmService;
import fun.tan90.easy.log.common.constant.EasyLogConstants;
import fun.tan90.easy.log.core.model.LogAlarmContent;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttPublishMessage;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.core.client.IMqttClientMessageListener;
import net.dreamlu.iot.mqtt.spring.client.MqttClientSubscribe;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;

import javax.annotation.Resource;
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
@MqttClientSubscribe(value = "$share/admin/" + EasyLogConstants.MQTT_LOG_ALARM_TOPIC, qos = MqttQoS.EXACTLY_ONCE)
public class LogAlarmMessageListener implements IMqttClientMessageListener {

    @Resource
    LogAlarmService logAlarmService;

    @Override
    public void onMessage(ChannelContext context, String topic, MqttPublishMessage message, byte[] payload) {
        String msg = new String(payload, StandardCharsets.UTF_8);
        log.info("topic:{} payload:{}", topic, msg);
        LogAlarmContent logAlarmContent = JSONUtil.toBean(msg, LogAlarmContent.class);
        logAlarmService.handlerLogAlarm(logAlarmContent);
    }
}

