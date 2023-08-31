package fun.tan90.easy.log.admin.service.impl;

import cn.hutool.json.JSONUtil;
import fun.tan90.easy.log.admin.service.SysMonitorService;
import fun.tan90.easy.log.common.constant.EasyLogConstants;
import fun.tan90.easy.log.common.threadpool.EasyLogThreadPool;
import fun.tan90.easy.log.core.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.spring.client.MqttClientTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/25 15:14
 */
@Slf4j
@Service
public class SysMonitorServiceImpl implements SysMonitorService {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    CacheService cacheService;

    @Resource
    MqttClientTemplate mqttClientTemplate;

    @Override
    public void statsLogInputSpeed() {
        EasyLogThreadPool.newEasyLogScheduledExecutorInstance().scheduleWithFixedDelay(() -> {
            try {
                Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(EasyLogConstants.LOG_INPUT_SPEED_LOCK, "", 4, TimeUnit.SECONDS);
                if (Boolean.TRUE.equals(lock)) {
                    try {
                        Map<String, Integer> windowCountMap = cacheService.slidingWindowCount(EasyLogConstants.S_W_LOG_INPUT_SPEED);
                        mqttClientTemplate.publish(EasyLogConstants.MQTT_LOG_INPUT_SPEED_TOPIC, JSONUtil.toJsonStr(windowCountMap).getBytes(StandardCharsets.UTF_8), MqttQoS.AT_MOST_ONCE);
                    } finally {
                        stringRedisTemplate.delete(EasyLogConstants.LOG_INPUT_SPEED_LOCK);
                    }
                }
            } catch (Exception e) {
                log.error("statsLogInputSpeed {}", e.getMessage());
            }
        }, 1, 2, TimeUnit.SECONDS);
    }
}
