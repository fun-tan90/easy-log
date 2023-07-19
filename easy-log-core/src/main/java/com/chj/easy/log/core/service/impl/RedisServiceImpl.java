package com.chj.easy.log.core.service.impl;

import cn.hutool.core.lang.Singleton;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
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
public class RedisServiceImpl implements RedisService {

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

    @Override
    public int slidingWindow(String key, int period) {
        DefaultRedisScript<String> actual = Singleton.get(EasyLogConstants.SLIDING_WINDOW_PATH, () -> {
            DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(EasyLogConstants.SLIDING_WINDOW_PATH)));
            redisScript.setResultType(String.class);
            return redisScript;
        });
        String execute = stringRedisTemplate.execute(actual, Collections.singletonList(key), String.valueOf(period), String.valueOf(System.currentTimeMillis()));
        return Integer.parseInt(StringUtils.hasLength(execute) ? execute : "0");
    }

    @Override
    public Map<String, Integer> slidingWindowCount(String keyPrefix) {
        DefaultRedisScript<String> actual = Singleton.get(EasyLogConstants.SLIDING_WINDOW_COUNT_PATH, () -> {
            DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(EasyLogConstants.SLIDING_WINDOW_COUNT_PATH)));
            redisScript.setResultType(String.class);
            return redisScript;
        });
        String execute = stringRedisTemplate.execute(actual, Collections.singletonList(keyPrefix));
        if (StringUtils.hasLength(execute)) {
            return Arrays.stream(execute.split(",")).collect(Collectors.toMap(
                    n -> n.split("#")[0].replace(keyPrefix, ""),
                    m -> Integer.parseInt(m.split("#")[1])));
        }
        return new HashMap<>();
    }
}
