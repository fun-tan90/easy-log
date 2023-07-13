package com.chj.easy.log.server.collector.config;

import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.server.collector.property.EasyLogCollectorProperties;
import com.chj.easy.log.server.collector.stream.RedisStreamMessageListener;
import com.chj.easy.log.server.common.model.LogDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:50
 */
@Slf4j
@AutoConfigureAfter(EasyLogCollectorAutoConfiguration.class)
public class RedisStreamAutoConfiguration {

    @Bean
    public RedisStreamMessageListener redisStreamMessageListener(BlockingQueue<LogDoc> logDocBlockingQueue, StringRedisTemplate stringRedisTemplate) {
        return new RedisStreamMessageListener(logDocBlockingQueue, stringRedisTemplate);
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
    public List<Subscription> subscriptions(EasyLogCollectorProperties easyLogCollectorProperties,
                                            StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer,
                                            RedisStreamMessageListener redisStreamMessageListener) {
        List<Subscription> subscriptions = new ArrayList<>();
        for (int consumerNum : easyLogCollectorProperties.getConsumerNums()) {
            Subscription subscription = streamMessageListenerContainer
                    .receive(
                            Consumer.from(EasyLogConstants.GROUP_NAME, EasyLogConstants.GROUP_CONSUMER_NAME + "-" + consumerNum),
                            StreamOffset.create(EasyLogConstants.STREAM_KEY, ReadOffset.lastConsumed()),
                            redisStreamMessageListener);
            subscriptions.add(subscription);
        }
        return subscriptions;
    }
}
