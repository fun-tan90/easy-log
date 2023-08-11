package com.chj.easy.log.meter;

import io.micrometer.core.instrument.step.StepRegistryConfig;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/11 16:29
 */
public interface RedisStreamRegistryConfig extends StepRegistryConfig {

    RedisStreamRegistryConfig DEFAULT = k -> null;

    @Override
    default String prefix() {
        return "redis-stream";
    }

}
