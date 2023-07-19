package com.chj.easy.log.core.service;

import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;

import java.util.List;
import java.util.Map;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 10:59
 */
public interface RedisService {

    void initStream(String streamKey, String groupName, String consumerNamePrefix, int[] consumerGlobalOrders, StreamListener<String, MapRecord<String, String, String>> streamListener);

    int slidingWindow(String key, int period);

    Map<String,Integer> slidingWindowCount(String keyPrefix);
}