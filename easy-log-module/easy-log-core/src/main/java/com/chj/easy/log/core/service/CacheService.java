package com.chj.easy.log.core.service;

import com.chj.easy.log.core.model.*;

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
     * 添加日志告警规则
     *
     * @param logAlarmRule
     */
    void addLogAlarmRule(LogAlarmRule logAlarmRule);

    void addLogRealTimeFilterRule(LogRealTimeFilterRule logRealTimeFilterRule);

    void delLogRealTimeFilterRule(String clientId);

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