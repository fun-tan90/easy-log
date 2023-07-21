package com.chj.easy.log.compute.stream;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.model.SlidingWindow;
import com.chj.easy.log.core.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.spring.server.MqttServerTemplate;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/16 8:40
 */
@Slf4j
@Component
public class RedisStreamComputeMessageListener implements StreamListener<String, MapRecord<String, String, String>> {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    RedisService redisService;

    @Resource
    MqttServerTemplate mqttServerTemplate;

    @Override
    public void onMessage(MapRecord<String, String, String> entries) {
        if (entries != null) {
            Map<String, String> logMap = entries.getValue();
            CompletableFuture<Void> cfAll = CompletableFuture.allOf(logInputSpeed(logMap), logAlarm(logMap), logRealTimeFilter(logMap));
            cfAll.whenComplete((v, e) -> stringRedisTemplate.opsForStream().acknowledge(EasyLogConstants.REDIS_STREAM_KEY, EasyLogConstants.GROUP_COMPUTE_NAME, entries.getId().getValue()));
        }
    }

    /**
     * 日志收集速率
     */
    private CompletableFuture<Void> logInputSpeed(Map<String, String> logMap) {
        String level = logMap.get("level");
        String timeStamp = logMap.get("timeStamp");
        return CompletableFuture.runAsync(() -> redisService.slidingWindow("S_W:LOG_INPUT_SPEED:" + level, Long.parseLong(timeStamp), 5), EasyLogManager.EASY_LOG_FIXED_THREAD_POOL);
    }

    /**
     * 日志告警
     *
     * @param logMap
     */
    private CompletableFuture<Void> logAlarm(Map<String, String> logMap) {
        return CompletableFuture.runAsync(() -> {
            String level = logMap.get("level");
            if ("info".equalsIgnoreCase(level)) {
                String appName = logMap.get("appName");
                String timeStamp = logMap.get("timeStamp");
                SlidingWindow slidingWindow = redisService.slidingWindow("S_W:LOG_ALARM:" + appName, Long.parseLong(timeStamp), 5);
                log.info("滑动窗口内计数大小:{}", slidingWindow.getWindowCount());
            }
        }, EasyLogManager.EASY_LOG_FIXED_THREAD_POOL);
    }

    /**
     * 实时日志过滤
     *
     * @param logMap
     */
    private CompletableFuture<Void> logRealTimeFilter(Map<String, String> logMap) {
        return CompletableFuture.runAsync(() -> {
            Set<String> clientIds = stringRedisTemplate.opsForSet().members(EasyLogConstants.MQTT_ONLINE_CLIENTS);
            if (CollectionUtils.isEmpty(clientIds)) {
                return;
            }
            Iterator<String> clientIdsIterator = clientIds.iterator();
            while (clientIdsIterator.hasNext()) {
                String clientId = clientIdsIterator.next();
                String realTimeFilterRulesStr = stringRedisTemplate.opsForValue().get(EasyLogConstants.REAL_TIME_FILTER_RULES + clientId);
                if (StringUtils.hasLength(realTimeFilterRulesStr)) {
                    JSONObject realTimeFilterRules = JSONUtil.parseObj(realTimeFilterRulesStr);
                    for (String realTimeFilterRule : realTimeFilterRules.keySet()) {
                        String[] split = realTimeFilterRule.split("#");
                        String ruleKey = split[0];
                        String ruleWay = split[1];
                        String logVal = logMap.get(ruleKey);
                        String ruleVal = realTimeFilterRules.getStr(realTimeFilterRule);
                        if ("eq".equals(ruleWay)) {
                            if (!logVal.equals(ruleVal)) {
                                clientIdsIterator.remove();
                                break;
                            }
                        } else if ("should".equals(ruleWay)) {
                            List<String> list = Arrays.asList(ruleVal.split("%"));
                            Optional<String> any = list.stream().filter(logVal::contains).findAny();
                            if (!any.isPresent()) {
                                clientIdsIterator.remove();
                                break;
                            }
                        } else if ("gle".equals(ruleWay)) {
                            if (Long.parseLong(ruleVal) > Long.parseLong(logVal)) {
                                clientIdsIterator.remove();
                                break;
                            }
                        }
                    }
                } else {
                    clientIdsIterator.remove();
                }
            }
            if (!CollectionUtils.isEmpty(clientIds)) {
                log.info("日志过滤已完成，待发送客户端个数{}", clientIds.size());
                byte[] logBytes = JSONUtil.toJsonStr(logMap).getBytes(StandardCharsets.UTF_8);
                for (String clientId : clientIds) {
                    mqttServerTemplate.publish(clientId, EasyLogConstants.LOG_REAL_TIME_FILTERED_TOPIC, logBytes, MqttQoS.AT_MOST_ONCE);
                }
            }
        }, EasyLogManager.EASY_LOG_FIXED_THREAD_POOL);
    }
}
