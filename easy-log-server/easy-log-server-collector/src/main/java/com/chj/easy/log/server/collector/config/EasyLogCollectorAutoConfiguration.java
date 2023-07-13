package com.chj.easy.log.server.collector.config;

import com.chj.easy.log.server.collector.listener.AppReadyEventListener;
import com.chj.easy.log.server.collector.property.EasyLogCollectorProperties;
import com.chj.easy.log.server.common.mapper.LogDocMapper;
import com.chj.easy.log.server.common.model.LogDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

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
@RequiredArgsConstructor
@EnableConfigurationProperties(EasyLogCollectorProperties.class)
public class EasyLogCollectorAutoConfiguration {

    @Bean
    public BlockingQueue<LogDoc> logDocBlockingQueue(EasyLogCollectorProperties easyLogCollectorProperties) {
        return new LinkedBlockingQueue<>(easyLogCollectorProperties.getQueueCapacity());
    }

    @Bean
    public AppReadyEventListener appReadyEventProcessor(StringRedisTemplate stringRedisTemplate,
                                                        BlockingQueue<LogDoc> logDocBlockingQueue,
                                                        LogDocMapper logDocMapper,
                                                        EasyLogCollectorProperties easyLogCollectorProperties) {
        return new AppReadyEventListener(stringRedisTemplate, logDocBlockingQueue, logDocMapper, easyLogCollectorProperties);
    }
}
