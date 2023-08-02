package com.chj.easy.log.collector.config;

import com.chj.easy.log.collector.property.EasyLogCollectorProperties;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.model.LogDoc;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:50
 */
@ConditionalOnProperty(value = "easy-log.collector.enable", havingValue = "true")
@ComponentScan(EasyLogConstants.COLLECTOR_SCAN_BASE_PACKAGES)
@EnableConfigurationProperties(EasyLogCollectorProperties.class)
public class EasyLogCollectorAutoConfiguration {

    @Bean
    public BlockingQueue<LogDoc> logDocBlockingQueue(EasyLogCollectorProperties easyLogCollectorProperties) {
        return new LinkedBlockingQueue<>(easyLogCollectorProperties.getQueueCapacity());
    }
}
