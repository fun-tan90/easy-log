package com.chj.easy.log.meter;

import com.chj.easy.log.common.EasyLogManager;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Map;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:50
 */
public class MqttStepMeterRegistryRunner implements ApplicationRunner {

    @Resource
    private Map<String, MeterBinder> metersMap;

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
        metersMap.values().forEach(n -> n.bindTo(mqttMeterRegistry));
    }
}
