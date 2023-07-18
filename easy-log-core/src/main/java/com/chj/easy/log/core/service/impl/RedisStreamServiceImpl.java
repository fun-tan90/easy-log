package com.chj.easy.log.core.service.impl;

import com.chj.easy.log.core.service.RedisStreamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/18 20:33
 */
@Slf4j
@Service
public class RedisStreamServiceImpl implements RedisStreamService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer;

    @Override
    public void initStream(String streamKey,
                           String groupName,
                           String consumerNamePrefix,
                           int[] consumerGlobalOrders,
                           StreamListener<String, MapRecord<String, String, String>> streamListener) {
        Boolean hasKey = stringRedisTemplate.hasKey(streamKey);
        if (Boolean.FALSE.equals(hasKey)) {
            stringRedisTemplate.opsForStream().createGroup(streamKey, groupName);
        }
        StreamInfo.XInfoGroups groups = stringRedisTemplate.opsForStream().groups(streamKey);
        Optional<StreamInfo.XInfoGroup> xInfoGroupOpt = groups.stream().filter(n -> n.groupName().equals(groupName)).findAny();
        if (!xInfoGroupOpt.isPresent()) {
            stringRedisTemplate.opsForStream().createGroup(streamKey, groupName);
        }
        for (int consumerGlobalOrder : consumerGlobalOrders) {
            String consumerName = consumerNamePrefix + consumerGlobalOrder;
            streamMessageListenerContainer
                    .receive(
                            Consumer.from(groupName, consumerName),
                            StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
                            streamListener
                    );
        }
    }
}
