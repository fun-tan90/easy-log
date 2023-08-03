package com.chj.easy.log.event.cluster;

import com.chj.easy.log.event.enums.RedisKeys;
import com.chj.easy.log.event.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import net.dreamlu.iot.mqtt.core.server.model.Message;
import net.dreamlu.iot.mqtt.core.server.serializer.IMessageSerializer;
import net.dreamlu.iot.mqtt.core.server.store.IMqttMessageStore;
import net.dreamlu.iot.mqtt.core.util.TopicUtil;
import net.dreamlu.mica.redis.cache.MicaRedisCache;

import java.util.ArrayList;
import java.util.List;

/**
 * redis mqtt 遗嘱和保留消息存储
 *
 * @author L.cm
 */
@RequiredArgsConstructor
public class RedisMqttMessageStore implements IMqttMessageStore {

    private final MicaRedisCache redisCache;

    private final IMessageSerializer messageSerializer;

    @Override
    public boolean addWillMessage(String clientId, Message message) {
        redisCache.set(RedisKeys.MESSAGE_STORE_WILL.getKey(clientId), message, messageSerializer::serialize);
        return true;
    }

    @Override
    public boolean clearWillMessage(String clientId) {
        redisCache.del(RedisKeys.MESSAGE_STORE_WILL.getKey(clientId));
        return true;
    }

    @Override
    public Message getWillMessage(String clientId) {
        return redisCache.get(RedisKeys.MESSAGE_STORE_WILL.getKey(clientId), messageSerializer::deserialize);
    }

    @Override
    public boolean addRetainMessage(String topic, Message message) {
        redisCache.set(RedisKeys.MESSAGE_STORE_RETAIN.getKey(topic), message, messageSerializer::serialize);
        return true;
    }

    @Override
    public boolean clearRetainMessage(String topic) {
        redisCache.del(RedisKeys.MESSAGE_STORE_RETAIN.getKey(topic));
        return true;
    }

    @Override
    public List<Message> getRetainMessage(String topicFilter) {
        List<Message> retainMessageList = new ArrayList<>();
        RedisKeys redisKey = RedisKeys.MESSAGE_STORE_RETAIN;
        String redisKeyPrefix = redisKey.getKey();
        String redisKeyPattern = redisKeyPrefix.concat(RedisUtil.getTopicPattern(topicFilter));
        int keyPrefixLength = redisKeyPrefix.length();
        redisCache.scan(redisKeyPattern, (key) -> {
            String keySuffix = key.substring(keyPrefixLength);
            if (TopicUtil.match(topicFilter, keySuffix)) {
                Message message = redisCache.get(key, messageSerializer::deserialize);
                if (message != null) {
                    retainMessageList.add(message);
                }
            }
        });
        return retainMessageList;
    }

}
