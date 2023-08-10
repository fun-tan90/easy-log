package com.chj.easy.log.mqtt.config;

import net.dreamlu.iot.mqtt.core.server.model.Message;
import net.dreamlu.iot.mqtt.core.server.serializer.IMessageSerializer;
import net.dreamlu.mica.core.utils.JsonUtil;


/**
 * description jackson 消息序列化
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/29 21:36
 */
public class JacksonMessageSerializer implements IMessageSerializer {

    @Override
    public byte[] serialize(Message message) {
        return JsonUtil.toJsonAsBytes(message);
    }

    @Override
    public Message deserialize(byte[] data) {
        return JsonUtil.readValue(data, Message.class);
    }

}
