package com.chj.easy.log.compute.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 10:31
 */
@Data
@ConfigurationProperties(prefix = EasyLogComputeProperties.PREFIX)
public class EasyLogComputeProperties {

    public static final String PREFIX = "easy-log.compute";

    private boolean enable = true;

    private boolean banner = true;

    private int consumerGlobalOrder;

    private String statsLogSpeedCron = "0/5 * * * * ?";
}
