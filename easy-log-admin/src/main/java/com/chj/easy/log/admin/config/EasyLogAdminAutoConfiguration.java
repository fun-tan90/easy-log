package com.chj.easy.log.admin.config;

import com.chj.easy.log.admin.property.EasyLogAdminProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:50
 */
@Slf4j
@EnableScheduling
@EnableConfigurationProperties(EasyLogAdminProperties.class)
public class EasyLogAdminAutoConfiguration {
}