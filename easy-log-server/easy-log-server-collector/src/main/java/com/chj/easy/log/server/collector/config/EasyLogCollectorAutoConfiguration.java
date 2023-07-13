package com.chj.easy.log.server.collector.config;

import com.chj.easy.log.server.collector.listener.AppReadyEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:50
 */
@Slf4j
public class EasyLogCollectorAutoConfiguration {

    @Bean
    public AppReadyEventListener appReadyEventProcessor(StringRedisTemplate stringRedisTemplate) {
        return new AppReadyEventListener(stringRedisTemplate);
    }
}
