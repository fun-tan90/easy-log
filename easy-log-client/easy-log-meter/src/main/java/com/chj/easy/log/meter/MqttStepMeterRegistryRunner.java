package com.chj.easy.log.meter;

import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.annotation.Resource;
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
    Map<String, MeterBinder> metersMap;

    @Resource
    MqttMeterRegistry mqttMeterRegistry;

    @Override
    public void run(ApplicationArguments args) {
        metersMap.values().forEach(meter -> meter.bindTo(mqttMeterRegistry));
    }
}
