package com.chj.easy.log.core.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 10:31
 */
@Data
@ConfigurationProperties(prefix = EasyLogStreamProperties.PREFIX)
public class EasyLogStreamProperties {

    public static final String PREFIX = "easy-log.stream";

    private Duration pollTimeout = Duration.ofSeconds(1);

    private int pullBatchSize = 100;
}
