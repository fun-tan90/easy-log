package com.chj.easy.log.admin.mqtt.cluster;

import com.chj.easy.log.admin.mqtt.enums.RedisKeys;
import com.chj.easy.log.common.constant.EasyLogConstants;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.core.server.MqttServer;
import net.dreamlu.iot.mqtt.core.server.model.Message;
import net.dreamlu.iot.mqtt.core.server.serializer.IMessageSerializer;
import net.dreamlu.mica.core.utils.StringUtil;
import net.dreamlu.mica.redis.stream.MessageModel;
import net.dreamlu.mica.redis.stream.RStreamListener;
import org.springframework.data.redis.connection.stream.MapRecord;

import java.util.Map;

/**
 * 下行消息，发送到设备
 *
 * @author L.cm
 */
@Slf4j
public class RedisMqttMessageDownReceiver {

    private final IMessageSerializer messageSerializer;

    private final MqttServer mqttServer;

    public RedisMqttMessageDownReceiver(IMessageSerializer messageSerializer,
                                        MqttServer mqttServer) {
        this.messageSerializer = messageSerializer;
        this.mqttServer = mqttServer;
    }

    @RStreamListener(
            name = RedisKeys.REDIS_CHANNEL_DOWN_KEY,
            messageModel = MessageModel.BROADCASTING,
            readRawBytes = true
    )
    public void mqttMessageDownReceiver(MapRecord<String, String, byte[]> mapRecord) {
        // 手动序列化和反序列化，避免 redis 序列化不一致问题
        Map<String, byte[]> recordValue = mapRecord.getValue();
        recordValue.forEach((key, messageBody) -> {
            // 手动序列化和反序列化，避免 redis 序列化不一致问题
            Message mqttMessage = messageSerializer.deserialize(messageBody);
            if (mqttMessage == null) {
                return;
            }
            // 下行消息，发送到设备
            String topic = mqttMessage.getTopic();
            if (StringUtil.isBlank(topic)) {
                log.error("Mqtt down stream topic is blank.");
                return;
            }
            String clientId = mqttMessage.getClientId();
            byte[] payload = mqttMessage.getPayload();
            MqttQoS mqttQoS = MqttQoS.valueOf(mqttMessage.getQos());
            boolean retain = mqttMessage.isRetain();
            if (StringUtil.isBlank(clientId)) {
                mqttServer.publishAll(topic, payload, mqttQoS, retain);
            } else {
                mqttServer.publish(clientId, topic, payload, mqttQoS, retain);
            }
        });
    }

}
