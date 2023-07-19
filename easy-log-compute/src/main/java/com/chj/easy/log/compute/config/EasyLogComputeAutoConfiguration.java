package com.chj.easy.log.compute.config;

import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.compute.property.EasyLogComputeProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:50
 */
@Slf4j(topic = EasyLogConstants.LOG_TOPIC_COMPUTE)
@ConditionalOnProperty(value = "easy-log.compute.enable", havingValue = "true")
@ComponentScan(EasyLogConstants.COMPUTE_SCAN_BASE_PACKAGES)
@EnableConfigurationProperties(EasyLogComputeProperties.class)
public class EasyLogComputeAutoConfiguration {

}
