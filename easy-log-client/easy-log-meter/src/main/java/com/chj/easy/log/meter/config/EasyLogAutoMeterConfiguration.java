package com.chj.easy.log.meter.config;

import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.meter.MqttMeterRegistry;
import com.chj.easy.log.meter.MqttRegistryConfig;
import com.chj.easy.log.meter.MqttStepMeterRegistryRunner;
import com.chj.easy.log.meter.aop.CounterAspect;
import com.chj.easy.log.meter.jvm.JvmInfoMetrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.logging.Log4j2Metrics;
import io.micrometer.core.instrument.binder.logging.LogbackMetrics;
import io.micrometer.core.instrument.binder.system.DiskSpaceMetrics;
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.time.Duration;
import java.util.Collections;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/22 16:26
 */
public class EasyLogAutoMeterConfiguration {

    @Bean
    public MqttMeterRegistry mqttMeterRegistry() {
        return new MqttMeterRegistry(
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
    }

    @Bean
    public CounterAspect counterAspect(MqttMeterRegistry mqttMeterRegistry) {
        return new CounterAspect(mqttMeterRegistry);
    }

    @Bean
    public ClassLoaderMetrics classLoaderMetrics() {
        return new ClassLoaderMetrics(Collections.singletonList(Tag.of("metricType", "ClassLoaderMetrics")));
    }

    @Bean
    public JvmCompilationMetrics jvmCompilationMetrics() {
        return new JvmCompilationMetrics(Collections.singletonList(Tag.of("metricType", "JvmCompilationMetrics")));
    }

    @Bean
    public JvmGcMetrics jvmGcMetrics() {
        return new JvmGcMetrics(Collections.singletonList(Tag.of("metricType", "JvmGcMetrics")));
    }

    @Bean
    public JvmHeapPressureMetrics jvmHeapPressureMetrics() {
        return new JvmHeapPressureMetrics(Collections.singletonList(Tag.of("metricType", "JvmHeapPressureMetrics")), Duration.ofMinutes(5), Duration.ofMinutes(1));
    }

    @Bean
    public JvmInfoMetrics jvmInfoMetrics() {
        return new JvmInfoMetrics(Collections.singletonList(Tag.of("metricType", "JvmInfoMetrics")));
    }

    @Bean
    public JvmMemoryMetrics jvmMemoryMetrics() {
        return new JvmMemoryMetrics(Collections.singletonList(Tag.of("metricType", "JvmMemoryMetrics")));
    }

    @Bean
    public JvmThreadMetrics jvmThreadMetrics() {
        return new JvmThreadMetrics(Collections.singletonList(Tag.of("metricType", "JvmThreadMetrics")));
    }

    @Bean
    public DiskSpaceMetrics diskSpaceMetrics() {
        return new DiskSpaceMetrics(new File("."), Collections.singletonList(Tag.of("metricType", "DiskSpaceMetrics")));
    }

    @Bean
    public FileDescriptorMetrics fileDescriptorMetrics() {
        return new FileDescriptorMetrics(Collections.singletonList(Tag.of("metricType", "FileDescriptorMetrics")));
    }

    @Bean
    public ProcessorMetrics processorMetrics() {
        return new ProcessorMetrics(Collections.singletonList(Tag.of("metricType", "ProcessorMetrics")));
    }

    @Bean
    public UptimeMetrics uptimeMetrics() {
        return new UptimeMetrics(Collections.singletonList(Tag.of("metricType", "UptimeMetrics")));
    }

    @Bean
    @ConditionalOnClass(name = "ch.qos.logback.classic.LoggerContext")
    public LogbackMetrics logbackMetrics() {
        return new LogbackMetrics(Collections.singletonList(Tag.of("metricType", "LogbackMetrics")));
    }

    @Bean
    @ConditionalOnClass(name = "org.apache.logging.log4j.core.LoggerContext")
    public Log4j2Metrics log4j2Metrics() {
        return new Log4j2Metrics(Collections.singletonList(Tag.of("metricType", "Log4j2Metrics")));
    }

    @Bean
    public MqttStepMeterRegistryRunner mqttStepMeterRegistryRunner() {
        return new MqttStepMeterRegistryRunner();
    }
}
