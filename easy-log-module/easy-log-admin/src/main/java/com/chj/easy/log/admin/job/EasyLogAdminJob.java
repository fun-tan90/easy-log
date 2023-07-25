package com.chj.easy.log.admin.job;

import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.model.LogDoc;
import com.chj.easy.log.core.service.EsService;
import com.chj.easy.log.core.service.RedisService;
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
public class EasyLogAdminJob {

    @Resource
    RedisService redisService;

    @Resource
    MqttServerTemplate mqttServerTemplate;

    @Scheduled(cron = "${easy-log.compute.stats-log-speed-cron:0/2 * * * * ?}")
    public void statsLogInputSpeed() {
        Map<String, Integer> windowCountMap = redisService.slidingWindowCount("S_W:LOG_INPUT_SPEED:");
        mqttServerTemplate.publishAll(EasyLogConstants.LOG_INPUT_SPEED_TOPIC, JSONUtil.toJsonStr(windowCountMap).getBytes(StandardCharsets.UTF_8), MqttQoS.AT_MOST_ONCE);
    }
}
