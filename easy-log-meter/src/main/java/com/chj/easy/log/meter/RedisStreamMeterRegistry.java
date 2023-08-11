package com.chj.easy.log.meter;

import cn.hutool.core.util.StrUtil;
import com.chj.easy.log.common.model.MetricContext;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.config.NamingConvention;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import io.micrometer.core.instrument.util.NamedThreadFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/11 16:28
 */
public class RedisStreamMeterRegistry extends StepMeterRegistry {

    private final String appName;

    private final String namespace;

    private final String currIp;

    private final RedisStreamRegistryConfig config;

    public RedisStreamMeterRegistry(String appName, String namespace, String currIp, RedisStreamRegistryConfig config) {
        this(appName, namespace, currIp, config, Clock.SYSTEM, new NamedThreadFactory("redis-stream-publisher"));
    }

    private RedisStreamMeterRegistry(String appName, String namespace, String currIp, RedisStreamRegistryConfig config, Clock clock, ThreadFactory threadFactory) {
        super(config, clock);
        this.appName = appName;
        this.namespace = namespace;
        this.currIp = currIp;
        this.config = config;
        config().namingConvention(NamingConvention.dot);
        start(threadFactory);
    }

    @Override
    protected void publish() {
        if (config.enabled()) {
            getMeters().stream().sorted((m1, m2) -> {
                int typeComp = m1.getId().getType().compareTo(m2.getId().getType());
                if (typeComp == 0) {
                    return m1.getId().getName().compareTo(m2.getId().getName());
                }
                return typeComp;
            }).forEach(meter -> {
                Meter.Id id = meter.getId();
                String name = convertName(id.getName());
                Meter.Type type = id.getType();
                Map<String, String> tagMap = id.getTags().stream()
                        .collect(Collectors.toMap(tag -> StrUtil.toUnderlineCase(tag.getKey()), Tag::getValue));
                Iterator<Measurement> iterator = meter.measure().iterator();
                Measurement next = iterator.next();
                new MetricContext.Meter(
                        name,
                        type.name().toLowerCase(),
                        tagMap,
                        next.getValue()
                );
            });
        }
    }

    private String convertName(String name) {
        return StrUtil.isBlank(name) ? "unknown" : name.replace(".", "_");
    }

    @Override
    protected TimeUnit getBaseTimeUnit() {
        return TimeUnit.MILLISECONDS;
    }
}
