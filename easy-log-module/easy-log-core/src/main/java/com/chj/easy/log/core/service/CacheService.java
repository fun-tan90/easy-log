package com.chj.easy.log.core.service;

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

    /**
     * 初始化redis stream 的组和组内消费者
     *
     * @param streamKey
     * @param groupName
     * @param consumerNamePrefix
     * @param consumerGlobalOrders
     * @param streamListener
     */
    void initGroupAndConsumers(String streamKey, String groupName, String consumerNamePrefix, int[] consumerGlobalOrders, StreamListener<String, MapRecord<String, String, String>> streamListener);

    /**
     * 滑动窗口算法
     *
     * @param key
     * @param unique
     * @param timestamp
     * @param period
     * @return
     */
    SlidingWindow slidingWindow(String key, String unique, long timestamp, int period);

    /**
     * 滑动窗口内各个key的统计
     *
     * @param keyPrefix
     * @return
     */
    Map<String, Integer> slidingWindowCount(String keyPrefix);

    /**
     * 新增实时日志过滤规则
     *
     * @param mqttClientId
     * @param realTimeFilterRules
     */
    void addLogRealTimeFilterRule(String mqttClientId, Map<String, String> realTimeFilterRules);

    /**
     * 删除实时日志过滤规则
     *
     * @param mqttClientId
     */
    void delLogRealTimeFilterRule(String mqttClientId);

    /**
     * 获取实时日志过滤规则
     *
     * @param mqttClientId
     * @return
     */
    Map<String, String> getLogRealTimeFilterRule(String mqttClientId);

    /**
     * 添加实时日志订阅客户端
     *
     * @param mqttClientId
     */
    void addRealTimeFilterSubscribingClient(String mqttClientId);

    /**
     * 删除实时日志订阅客户端
     *
     * @param mqttClientId
     */
    void delRealTimeFilterSubscribingClient(String mqttClientId);

    /**
     * 获取全部订阅中的客户端ID
     *
     * @return
     */
    Set<String> getRealTimeFilterSubscribingClients();

    /**
     * 添加过滤后的日志
     *
     * @param clientId
     * @param logMap
     * @param score
     */
    void addRealTimeFilteredLogs(String clientId, Map<String, String> logMap, double score);

    /**
     * 获取过滤后的日志
     *
     * @param clientId
     * @return
     */
    List<String> popRealTimeFilteredLog(String clientId);

    /**
     * 添加日志告警上下文信息
     *
     * @param logAlarmContent
     */
    void addLogAlarmContent(LogAlarmContent logAlarmContent);

    /**
     * 获取日志告警上下文信息
     *
     * @param timeout
     * @return
     */
    String popLogAlarmContent(long timeout);

    /**
     * 添加日志告警规则
     *
     * @param logAlarmRule
     * @return
     */
    String addLogAlarmRule(LogAlarmRule logAlarmRule);

    /**
     * 添加日志告警平台信息
     *
     * @param alarmPlatformType
     * @param logAlarmPlatform
     * @return
     */
    String addAlarmPlatform(String alarmPlatformType, LogAlarmPlatform logAlarmPlatform);

    /**
     * 获取日志告警平台信息
     *
     * @param alarmPlatformType
     * @param alarmPlatformId
     * @return
     */
    LogAlarmPlatform getAlarmPlatform(String alarmPlatformType, String alarmPlatformId);

    /**
     * 删除日志告警平台信息
     *
     * @param alarmPlatformType
     * @param alarmPlatformId
     * @return
     */
    void delAlarmPlatform(String alarmPlatformType, String alarmPlatformId);
}