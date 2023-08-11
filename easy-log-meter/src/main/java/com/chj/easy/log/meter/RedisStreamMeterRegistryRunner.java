package com.chj.easy.log.meter;

import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.time.Duration;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:50
 */
public class RedisStreamMeterRegistryRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        RedisStreamMeterRegistry redisStreamMeterRegistry = new RedisStreamMeterRegistry(
                "",
                "",
                new RedisStreamRegistryConfig() {
                    @Override
                    public String get(String key) {
                        System.out.println(key);
                        return null;
                    }

                    @Override
                    public Duration step() {
                        return Duration.ofSeconds(5);
                    }
                });
        new ProcessorMetrics().bindTo(redisStreamMeterRegistry);
        new JvmMemoryMetrics().bindTo(redisStreamMeterRegistry);
    }
}
