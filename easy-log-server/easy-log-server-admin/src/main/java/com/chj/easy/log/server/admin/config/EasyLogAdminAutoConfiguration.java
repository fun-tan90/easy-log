package com.chj.easy.log.server.admin.config;

import com.chj.easy.log.server.admin.listener.AdminInitListener;
import com.chj.easy.log.server.admin.property.EasyLogAdminProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:50
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(EasyLogAdminProperties.class)
public class EasyLogAdminAutoConfiguration {
}