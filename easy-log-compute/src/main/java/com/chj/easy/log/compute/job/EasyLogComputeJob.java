package com.chj.easy.log.compute.job;

import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.constant.EasyLogConstants;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.spring.server.MqttServerTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 17:34
 */
@Slf4j
@Component
public class EasyLogComputeJob {

    @Resource
    MqttServerTemplate mqttServerTemplate;

    @Scheduled(cron = "${easy-log.compute.stats-log-speed-cron:0/2 * * * * ?}")
    public void statsLogInputSpeed() {
        // TODO
//        Map<String, Integer> statsLogInputSpeed = EasyLogManager.statsLogInputSpeed();
//        mqttServerTemplate.publishAll(EasyLogConstants.INPUT_SPEED_TOPIC, JSONUtil.toJsonStr(statsLogInputSpeed).getBytes(StandardCharsets.UTF_8), MqttQoS.AT_MOST_ONCE);
    }
}
