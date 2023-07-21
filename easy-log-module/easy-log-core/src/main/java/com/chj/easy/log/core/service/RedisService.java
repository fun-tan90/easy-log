package com.chj.easy.log.core.service;

import com.chj.easy.log.core.model.SlidingWindow;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;

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

    SlidingWindow slidingWindow(String key, String unique, long timestamp, int period);

    Map<String, Integer> slidingWindowCount(String keyPrefix);
}