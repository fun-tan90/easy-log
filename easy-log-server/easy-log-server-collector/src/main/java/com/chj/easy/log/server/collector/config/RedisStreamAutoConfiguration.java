package com.chj.easy.log.server.collector.config;

import cn.hutool.core.util.RandomUtil;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.constant.EasyLogConstants;
import lombok.extern.slf4j.Slf4j;
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

//
//    @Bean(initMethod = "start", destroyMethod = "stop")
//    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer(RedisConnectionFactory factory,
//                                                                                                                    StringRedisTemplate stringRedisTemplate,
//                                                                                                                    RedisStreamMessageListener redisStreamMessageListener) {
//        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
//                StreamMessageListenerContainer
//                        .StreamMessageListenerContainerOptions
//                        .builder()
//                        .pollTimeout(Duration.ofSeconds(1))
//                        .batchSize(100)
//                        .build();
//
//        StreamMessageListenerContainer<String, MapRecord<String, String, String>> listenerContainer =
//                StreamMessageListenerContainer.create(factory, options);
//
////        Boolean hasKey = stringRedisTemplate.hasKey(EasyLogConstants.STREAM_KEY);
////        if (Boolean.FALSE.equals(hasKey)) {
////            Map<String, Object> content = new HashMap<>();
////            content.put("field", "value");
////            RecordId recordId = stringRedisTemplate.opsForStream().add(EasyLogConstants.STREAM_KEY, content);
////            Assert.notNull(recordId, "RecordId must not be null");
////            stringRedisTemplate.opsForStream().createGroup(EasyLogConstants.STREAM_KEY, EasyLogConstants.GROUP_NAME);
////            stringRedisTemplate.opsForStream().delete(EasyLogConstants.STREAM_KEY, recordId.getValue());
////        }
//
//        listenerContainer
//                .receive(
//                        Consumer.from(EasyLogConstants.GROUP_NAME, RandomUtil.randomNumbers(2)),
//                        StreamOffset.create(EasyLogConstants.STREAM_KEY, ReadOffset.lastConsumed()),
//                        redisStreamMessageListener);
//
//        return listenerContainer;
//    }
}
