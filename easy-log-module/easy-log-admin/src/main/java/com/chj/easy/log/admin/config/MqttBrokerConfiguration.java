package com.chj.easy.log.admin.config;

import com.chj.easy.log.admin.mqtt.cluster.*;
import com.chj.easy.log.admin.mqtt.config.JacksonMessageSerializer;
import com.chj.easy.log.admin.mqtt.enums.RedisKeys;
import com.chj.easy.log.admin.mqtt.listener.MqttServerMessageListener;
import com.chj.easy.log.admin.mqtt.listener.MqttSessionListener;
import com.chj.easy.log.admin.mqtt.listener.RedisMqttConnectStatusListener;
import com.chj.easy.log.core.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.core.server.MqttServer;
import net.dreamlu.iot.mqtt.core.server.dispatcher.IMqttMessageDispatcher;
import net.dreamlu.iot.mqtt.core.server.event.IMqttConnectStatusListener;
import net.dreamlu.iot.mqtt.core.server.serializer.IMessageSerializer;
import net.dreamlu.iot.mqtt.core.server.store.IMqttMessageStore;
import net.dreamlu.mica.redis.cache.MicaRedisCache;
import net.dreamlu.mica.redis.stream.RStreamTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * mica mqtt broker 配置
 *
 * @author L.cm
 */
@Slf4j
@ConditionalOnProperty(value = "easy-log.admin.enable", havingValue = "true")
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
    public IMqttMessageDispatcher mqttMessageDispatcher(RStreamTemplate streamTemplate,
                                                        IMessageSerializer messageSerializer) {
        return new RedisMqttMessageDispatcher(streamTemplate, messageSerializer, RedisKeys.REDIS_CHANNEL_EXCHANGE.getKey());
    }

    @Bean
    public RedisMqttServerManager mqttServerManage(MicaRedisCache redisCache,
                                                   MqttServer mqttServer) {
        return new RedisMqttServerManager(redisCache, mqttServer);
    }

    @Bean
    public MqttServerMessageListener mqttServerMessageListener() {
        return new MqttServerMessageListener();
    }

    @Bean
    public MqttSessionListener mqttSessionListener(CacheService cacheService, ApplicationContext applicationContext) {
        return new MqttSessionListener(cacheService, applicationContext);
    }
}
