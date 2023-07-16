package com.chj.easy.log.admin.config;

import com.chj.easy.log.admin.property.EasyLogAdminProperties;
import com.chj.easy.log.common.constant.EasyLogConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:50
 */
@Slf4j
@ConditionalOnProperty(value = "easy-log.admin.enable", havingValue = "true")
@ComponentScan(EasyLogConstants.ADMIN_SCAN_BASE_PACKAGES)
@EnableScheduling
@EnableConfigurationProperties(EasyLogAdminProperties.class)
public class EasyLogAdminAutoConfiguration {
}