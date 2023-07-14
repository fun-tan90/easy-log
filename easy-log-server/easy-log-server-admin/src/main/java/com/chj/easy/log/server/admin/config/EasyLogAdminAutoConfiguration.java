package com.chj.easy.log.server.admin.config;

import com.chj.easy.log.server.admin.listener.AppReadyEventListener;
import com.chj.easy.log.server.admin.property.EasyLogAdminProperties;
import com.chj.easy.log.server.common.model.LogDoc;
import com.chj.easy.log.server.common.service.EsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:50
 */
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(EasyLogAdminProperties.class)
public class EasyLogAdminAutoConfiguration {

    @Bean
    public AppReadyEventListener appReadyEventProcessor(EsService<LogDoc> esService) {
        return new AppReadyEventListener(esService);
    }
}