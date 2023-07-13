package com.chj.easy.log.server.collector.config;

import com.chj.easy.log.common.constant.EasyLogConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;

/**
 * @author Huhailong
 * @Description 监听消息
 * @Date 2021/3/10.
 */
@Slf4j
@RequiredArgsConstructor
public class RedisStreamMessageListener implements StreamListener<String, MapRecord<String, String, String>> {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(MapRecord<String, String, String> entries) {
        log.info("接受到来自redis的消息");
        if (entries != null) {
            String stream = entries.getStream();
            System.out.println("message id " + entries.getId());
            System.out.println("stream " + stream);
            System.out.println("value " + entries.getValue());

            stringRedisTemplate.opsForStream().acknowledge(EasyLogConstants.STREAM_KEY, EasyLogConstants.GROUP_NAME, entries.getId().getValue());
            stringRedisTemplate.opsForStream().delete(EasyLogConstants.STREAM_KEY, entries.getId().getValue());
        }
    }

}
