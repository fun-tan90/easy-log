package com.chj.easy.log.meter;

import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.meter.jvm.JvmInfoMetrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.time.Duration;
import java.util.Collections;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:50
 */
public class MqttStepMeterRegistryRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        MqttMeterRegistry mqttMeterRegistry = new MqttMeterRegistry(
                EasyLogManager.GLOBAL_CONFIG.getAppName(),
                EasyLogManager.GLOBAL_CONFIG.getNamespace(),
                new MqttRegistryConfig() {
                    @Override
                    public String get(String key) {
                        if (key.endsWith("enabled")) {
                            return String.valueOf(EasyLogManager.GLOBAL_CONFIG.isEnableMeter());
                        }
                        return null;
                    }

                    @Override
                    public Duration step() {
                        return Duration.ofSeconds(5);
                    }
                });
        new ProcessorMetrics(Collections.singletonList(Tag.of("metricType", "ProcessorMetrics"))).bindTo(mqttMeterRegistry);
        new JvmGcMetrics(Collections.singletonList(Tag.of("metricType", "JvmGcMetrics"))).bindTo(mqttMeterRegistry);
        new JvmMemoryMetrics(Collections.singletonList(Tag.of("metricType", "JvmMemoryMetrics"))).bindTo(mqttMeterRegistry);
        new JvmThreadMetrics(Collections.singletonList(Tag.of("metricType", "JvmThreadMetrics"))).bindTo(mqttMeterRegistry);
        new JvmInfoMetrics(Collections.singletonList(Tag.of("metricType", "JvmInfoMetrics"))).bindTo(mqttMeterRegistry);
    }
}
