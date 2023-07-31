package com.chj.easy.log.admin.mqtt.cluster;

import net.dreamlu.iot.mqtt.core.server.dispatcher.IMqttMessageDispatcher;
import net.dreamlu.iot.mqtt.core.server.model.Message;
import net.dreamlu.iot.mqtt.core.server.serializer.IMessageSerializer;
import net.dreamlu.mica.redis.stream.RStreamTemplate;

import java.util.Objects;


/**
 * description redis 消息转发器
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/29 21:36
 */
public class RedisMqttMessageDispatcher implements IMqttMessageDispatcher {

    private final RStreamTemplate streamTemplate;

    private final IMessageSerializer messageSerializer;

    private final String channel;

    public RedisMqttMessageDispatcher(RStreamTemplate streamTemplate,
                                      IMessageSerializer messageSerializer,
                                      String channel) {
        this.streamTemplate = streamTemplate;
        this.messageSerializer = messageSerializer;
        this.channel = Objects.requireNonNull(channel, "Redis pub/sub channel is null.");
    }

    @Override
    public boolean send(Message message) {
        // 手动序列化和反序列化，避免 redis 序列化不一致问题
        String topic = message.getTopic();
        String key = topic == null ? message.getClientId() : topic;
        streamTemplate.send(channel, key, message, messageSerializer::serialize);
        return true;
    }

}
