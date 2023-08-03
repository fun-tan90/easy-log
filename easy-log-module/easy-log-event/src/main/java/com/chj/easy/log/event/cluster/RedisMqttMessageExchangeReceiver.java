package com.chj.easy.log.event.cluster;

import com.chj.easy.log.event.enums.RedisKeys;
import net.dreamlu.iot.mqtt.core.server.MqttServer;
import net.dreamlu.iot.mqtt.core.server.cluster.MqttClusterMessageListener;
import net.dreamlu.iot.mqtt.core.server.model.Message;
import net.dreamlu.iot.mqtt.core.server.serializer.IMessageSerializer;
import net.dreamlu.mica.redis.stream.MessageModel;
import net.dreamlu.mica.redis.stream.RStreamListener;
import org.springframework.data.redis.connection.stream.MapRecord;

import java.util.Map;

/**
 * 监听集群消息，上行和内部集群通道
 *
 * @author L.cm
 */
public class RedisMqttMessageExchangeReceiver {

    private final IMessageSerializer messageSerializer;
    private final MqttClusterMessageListener clusterMessageListener;

    public RedisMqttMessageExchangeReceiver(IMessageSerializer messageSerializer,
                                            MqttServer mqttServer) {
        this.messageSerializer = messageSerializer;
        this.clusterMessageListener = new MqttClusterMessageListener(mqttServer);
    }

    @RStreamListener(
            name = RedisKeys.REDIS_CHANNEL_EXCHANGE_KEY,
            messageModel = MessageModel.BROADCASTING,
            readRawBytes = true
    )
    public void mqttMessageUpReceiver(MapRecord<String, String, byte[]> mapRecord) {
        // 手动序列化和反序列化，避免 redis 序列化不一致问题
        Map<String, byte[]> recordValue = mapRecord.getValue();
        recordValue.forEach((key, messageBody) -> {
            // 手动序列化和反序列化，避免 redis 序列化不一致问题
            Message mqttMessage = messageSerializer.deserialize(messageBody);
            if (mqttMessage == null) {
                return;
            }
            clusterMessageListener.onMessage(mqttMessage);
        });
    }

}
