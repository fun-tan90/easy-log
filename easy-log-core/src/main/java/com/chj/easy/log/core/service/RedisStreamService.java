package com.chj.easy.log.core.service;

import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 10:59
 */
public interface RedisStreamService {

    void initStream(String streamKey, String groupName, String consumerNamePrefix, int[] consumerGlobalOrders, StreamListener<String, MapRecord<String, String, String>> streamListener);
}