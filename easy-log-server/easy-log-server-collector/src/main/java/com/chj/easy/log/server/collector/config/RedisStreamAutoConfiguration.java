package com.chj.easy.log.server.collector.config;

import cn.hutool.core.util.RandomUtil;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.server.collector.stream.RedisStreamMessageListener;
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

import java.time.Duration;

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
    public RedisStreamMessageListener redisStreamMessageListener(StringRedisTemplate stringRedisTemplate) {
        return new RedisStreamMessageListener(stringRedisTemplate);
    }

    @Bean
    public StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> streamMessageListenerContainerOptions() {
        return StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))
                .batchSize(100)
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
    public Subscription subscription(StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer,
                                     RedisStreamMessageListener redisStreamMessageListener) {
        return streamMessageListenerContainer
                .receive(
                        Consumer.from(EasyLogConstants.GROUP_NAME, RandomUtil.randomNumbers(2)),
                        StreamOffset.create(EasyLogConstants.STREAM_KEY, ReadOffset.lastConsumed()),
                        redisStreamMessageListener);
    }
}
