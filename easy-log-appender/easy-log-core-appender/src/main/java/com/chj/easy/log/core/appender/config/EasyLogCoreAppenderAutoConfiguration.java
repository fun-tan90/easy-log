package com.chj.easy.log.core.appender.config;

import com.chj.easy.log.core.appender.listener.ApplicationReadyEventListener;
import org.springframework.context.annotation.Bean;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:50
 */
public class EasyLogCoreAppenderAutoConfiguration {
    @Bean
    public ApplicationReadyEventListener applicationReadyEventListener() {
        return new ApplicationReadyEventListener();
    }
}