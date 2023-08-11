package com.chj.easy.log.meter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.model.MeterContext;
import com.chj.easy.log.common.utils.LocalhostUtil;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.config.NamingConvention;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import io.micrometer.core.instrument.util.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;
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
@Slf4j
public class RedisStreamMeterRegistry extends StepMeterRegistry {

    private final String appName;

    private final String namespace;

    private final static String CURR_IP = LocalhostUtil.getHostIp();

    private final RedisStreamRegistryConfig config;

    public RedisStreamMeterRegistry(String appName, String namespace, RedisStreamRegistryConfig config) {
        this(appName, namespace, config, Clock.SYSTEM, new NamedThreadFactory("redis-stream-publisher"));
    }

    private RedisStreamMeterRegistry(String appName, String namespace, RedisStreamRegistryConfig config, Clock clock, ThreadFactory threadFactory) {
        super(config, clock);
        this.appName = appName;
        this.namespace = namespace;
        this.config = config;
        config().namingConvention(NamingConvention.camelCase);
        start(threadFactory);
    }

    @Override
    protected void publish() {
        if (config.enabled()) {
            List<MeterContext.Meter> meters = getMeters().stream().sorted((m1, m2) -> {
                int typeComp = m1.getId().getType().compareTo(m2.getId().getType());
                if (typeComp == 0) {
                    return m1.getId().getName().compareTo(m2.getId().getName());
                }
                return typeComp;
            }).map(meter -> {
                Meter.Id id = meter.getId();
                String name = convertName(id.getName());
                Meter.Type type = id.getType();
                Map<String, String> tagMap = id.getTags().stream()
                        .collect(Collectors.toMap(tag -> StrUtil.toUnderlineCase(tag.getKey()), Tag::getValue));
                Iterator<Measurement> iterator = meter.measure().iterator();
                Measurement next = iterator.next();
                return new MeterContext.Meter(
                        name,
                        type.name().toLowerCase(),
                        tagMap,
                        next.getValue()
                );

            }).collect(Collectors.toList());
            MeterContext meterContext = MeterContext.builder()
                    .timeStamp(System.currentTimeMillis())
                    .appName(appName)
                    .namespace(namespace)
                    .currIp(CURR_IP)
                    .meters(meters).build();
            log.info(JSONUtil.toJsonPrettyStr(meterContext));
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
