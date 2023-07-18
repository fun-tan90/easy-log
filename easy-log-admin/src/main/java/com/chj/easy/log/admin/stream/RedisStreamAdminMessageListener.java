package com.chj.easy.log.admin.stream;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.common.window.SlidingWindow;
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
@Slf4j(topic = EasyLogConstants.LOG_TOPIC_ADMIN)
@Component
public class RedisStreamAdminMessageListener implements StreamListener<String, MapRecord<String, String, String>> {

    public static final SlidingWindow SLIDING_WINDOW = new SlidingWindow(1000, 5, 8);

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    MqttServerTemplate mqttServerTemplate;

    @Override
    public void onMessage(MapRecord<String, String, String> entries) {
        if (entries != null) {
            String recordId = entries.getId().getValue();
            Map<String, String> logMap = entries.getValue();
            CompletableFuture<Void> cfAll = CompletableFuture.allOf(logAlarm(logMap), logRealTimeFilter(logMap));
            cfAll.thenAccept(r -> {
                stringRedisTemplate.opsForStream().acknowledge(EasyLogConstants.STREAM_KEY, EasyLogConstants.GROUP_ADMIN_NAME, recordId);
            }).exceptionally(e -> {
                e.printStackTrace();
                stringRedisTemplate.opsForStream().acknowledge(EasyLogConstants.STREAM_KEY, EasyLogConstants.GROUP_ADMIN_NAME, recordId);
                return null;
            });
        }
    }

    /**
     * 日志告警
     *
     * @param logMap
     */
    private CompletableFuture<Void> logAlarm(Map<String, String> logMap) {
        return CompletableFuture.runAsync(() -> {
            boolean res = SLIDING_WINDOW.addCount(1);
            log.info("{}超过阈值", res ? "已" : "未");
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
                byte[] logBytes = JSONUtil.toJsonStr(logMap).getBytes(StandardCharsets.UTF_8);
                for (String clientId : clientIds) {
                    mqttServerTemplate.publish(clientId, EasyLogConstants.AFTER_FILTER_TOPIC, logBytes, MqttQoS.AT_MOST_ONCE);
                }
            }
        }, EasyLogManager.EASY_LOG_FIXED_THREAD_POOL);
    }
}
