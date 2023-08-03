package com.chj.easy.log.event.config;

import com.chj.easy.log.event.cluster.*;
import com.chj.easy.log.event.enums.RedisKeys;
import com.chj.easy.log.event.listener.MqttServerMessageListener;
import com.chj.easy.log.event.listener.MqttSessionListener;
import com.chj.easy.log.event.listener.RedisMqttConnectStatusListener;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.core.server.MqttServer;
import net.dreamlu.iot.mqtt.core.server.dispatcher.IMqttMessageDispatcher;
import net.dreamlu.iot.mqtt.core.server.event.IMqttConnectStatusListener;
import net.dreamlu.iot.mqtt.core.server.serializer.IMessageSerializer;
import net.dreamlu.iot.mqtt.core.server.store.IMqttMessageStore;
import net.dreamlu.mica.redis.cache.MicaRedisCache;
import net.dreamlu.mica.redis.stream.RStreamTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * mica mqtt broker 配置
 *
 * @author L.cm
 */
@Slf4j
public class MqttBrokerConfiguration {

    @Bean
    public IMessageSerializer messageSerializer() {
        return new JacksonMessageSerializer();
    }

    @Bean
    public IMqttConnectStatusListener mqttBrokerConnectListener(ApplicationContext applicationContext,
                                                                MicaRedisCache redisCache) {
        return new RedisMqttConnectStatusListener(applicationContext, redisCache);
    }

    @Bean
    public IMqttMessageStore mqttMessageStore(MicaRedisCache redisCache,
                                              IMessageSerializer messageSerializer) {
        return new RedisMqttMessageStore(redisCache, messageSerializer);
    }

    @Bean
    public RedisMqttMessageExchangeReceiver mqttMessageUpReceiver(IMessageSerializer messageSerializer,
                                                                  MqttServer mqttServer) {
        return new RedisMqttMessageExchangeReceiver(messageSerializer, mqttServer);
    }

    @Bean
    public RedisMqttMessageDownReceiver mqttMessageDownReceiver(IMessageSerializer messageSerializer,
                                                                MqttServer mqttServer) {
        return new RedisMqttMessageDownReceiver(messageSerializer, mqttServer);
    }

    @Bean
    public IMqttMessageDispatcher messageDispatcher(RStreamTemplate streamTemplate,
                                                    IMessageSerializer messageSerializer) {
        return new RedisMqttMessageDispatcher(streamTemplate, messageSerializer, RedisKeys.REDIS_CHANNEL_EXCHANGE.getKey());
    }

    @Bean
    public RedisMqttServerManager mqttServerManager(MicaRedisCache redisCache,
                                                    MqttServer mqttServer) {
        return new RedisMqttServerManager(redisCache, mqttServer);
    }

    @Bean
    public MqttServerMessageListener serverMessageListener() {
        return new MqttServerMessageListener();
    }

    @Bean
    public MqttSessionListener mqttSessionListener() {
        return new MqttSessionListener();
    }
}
