package com.chj.easy.log.server.collector.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 10:31
 */
@Data
@ConfigurationProperties(prefix = EasyLogCollectorProperties.PREFIX)
public class EasyLogCollectorProperties {

    public static final String PREFIX = "easy-log.collector";

    private Duration pollTimeout = Duration.ofSeconds(1);

    private int batchSize = 20;

    private int consumerNum = 1;

}
