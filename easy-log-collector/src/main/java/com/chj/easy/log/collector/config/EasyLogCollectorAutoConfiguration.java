package com.chj.easy.log.collector.config;

import com.chj.easy.log.collector.listener.CollectorInitListener;
import com.chj.easy.log.collector.property.EasyLogCollectorProperties;
import com.chj.easy.log.collector.stream.RedisStreamMessageListener;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.core.model.LogDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:50
 */
@Slf4j
@ConditionalOnProperty(value = "easy-log.collector.enable", havingValue = "true")
@EnableConfigurationProperties(EasyLogCollectorProperties.class)
public class EasyLogCollectorAutoConfiguration {

    @Bean
    public BlockingQueue<LogDoc> logDocBlockingQueue(EasyLogCollectorProperties easyLogCollectorProperties) {
        return new LinkedBlockingQueue<>(easyLogCollectorProperties.getQueueCapacity());
    }

    @Bean
    public StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> streamMessageListenerContainerOptions(EasyLogCollectorProperties easyLogCollectorProperties) {
        return StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(easyLogCollectorProperties.getPollTimeout())
                .batchSize(easyLogCollectorProperties.getPullBatchSize())
                .executor(EasyLogManager.EASY_LOG_FIXED_THREAD_POOL)
                .build();
    }

    @Bean
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer(RedisConnectionFactory factory,
                                                                                                                    StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> streamMessageListenerContainerOptions) {
        StreamMessageListenerContainer<String, MapRecord<String, String, String>> listenerContainer =
                StreamMessageListenerContainer.create(factory, streamMessageListenerContainerOptions);
        listenerContainer.start();
        return listenerContainer;
    }

    @Bean
    public RedisStreamMessageListener redisStreamMessageListener() {
        return new RedisStreamMessageListener();
    }

    @Bean
    public CollectorInitListener collectorInitListener() {
        return new CollectorInitListener();
    }
}
