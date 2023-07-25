package com.chj.easy.log.core.service;

import cn.hutool.json.JSONObject;
import com.chj.easy.log.core.model.LogAlarmContent;
import com.chj.easy.log.core.model.LogAlarmPlatform;
import com.chj.easy.log.core.model.LogAlarmRule;
import com.chj.easy.log.core.model.SlidingWindow;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 10:59
 */
public interface CacheService {

    void initGroupAndConsumers(String streamKey, String groupName, String consumerNamePrefix, int[] consumerGlobalOrders, StreamListener<String, MapRecord<String, String, String>> streamListener);

    SlidingWindow slidingWindow(String key, String unique, long timestamp, int period);

    Map<String, Integer> slidingWindowCount(String keyPrefix);

    void addLogRealTimeFilterRule(String mqttClientId, Map<String, String> realTimeFilterRules);

    void delLogRealTimeFilterRule(String mqttClientId);

    JSONObject getLogRealTimeFilterRule(String mqttClientId);

    void addRealTimeFilterSubscribingClient(String mqttClientId);

    void delRealTimeFilterSubscribingClient(String mqttClientId);

    Set<String> getRealTimeFilterSubscribingClients();

    void addRealTimeFilteredLogs(String clientId, Map<String, String> logMap, double score);

    List<String> popRealTimeFilteredLog(String clientId);

    void addLogAlarmContent(LogAlarmContent logAlarmContent);

    String popLogAlarmContent(long timeout);

    String addLogAlarmRule(LogAlarmRule logAlarmRule);

    String addAlarmPlatform(String alarmPlatformType, LogAlarmPlatform logAlarmPlatform);

    LogAlarmPlatform alarmPlatform(String alarmPlatformType, String alarmPlatformId);

    void delAlarmPlatform(String alarmPlatformType, String alarmPlatformId);
}