package com.chj.easy.log.meter.jvm;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.lang.NonNull;

import static java.util.Collections.emptyList;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/17 11:07
 */
public class JvmInfoMetrics implements MeterBinder {

    private final Iterable<Tag> tags;

    public JvmInfoMetrics() {
        this(emptyList());
    }

    public JvmInfoMetrics(Iterable<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public void bindTo(@NonNull MeterRegistry registry) {
        Gauge.builder("jvm.info", () -> 1L).description("JVM version info")
                .tags("version", System.getProperty("java.runtime.version", "unknown"),
                        "vendor", System.getProperty("java.vm.vendor", "unknown"),
                        "runtime", System.getProperty("java.runtime.name", "unknown")
                ).tags(tags)
                .strongReference(true).register(registry);
    }
}
