package com.chj.easy.log.admin.service.impl;

import com.chj.easy.log.admin.model.vo.RedisStreamXInfoVo;
import com.chj.easy.log.admin.service.SysRedisStreamService;
import com.chj.easy.log.common.constant.EasyLogConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.PendingMessage;
import org.springframework.data.redis.connection.stream.PendingMessages;
import org.springframework.data.redis.connection.stream.StreamInfo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/19 8:29
 */
@Slf4j
@Service
public class SysRedisStreamServiceImpl implements SysRedisStreamService {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public RedisStreamXInfoVo streamXInfo(String streamKey) {
        StreamInfo.XInfoGroups groups = stringRedisTemplate.opsForStream().groups(streamKey);
        List<RedisStreamXInfoVo.XInfoGroup> xInfoGroups = groups.stream().map(group -> {
            StreamInfo.XInfoConsumers consumers = stringRedisTemplate.opsForStream().consumers(EasyLogConstants.STREAM_KEY, group.groupName());

            List<RedisStreamXInfoVo.XInfoConsumer> xInfoConsumers = consumers.stream().map(xInfoConsumer -> RedisStreamXInfoVo.XInfoConsumer.builder()
                    .consumerName(xInfoConsumer.consumerName())
                    .pendingCount(xInfoConsumer.pendingCount())
                    .idleTimeMs(xInfoConsumer.idleTimeMs()).build()).collect(Collectors.toList());

            return RedisStreamXInfoVo.XInfoGroup.builder()
                    .groupName(group.groupName())
                    .consumers(xInfoConsumers).build();
        }).collect(Collectors.toList());

        return RedisStreamXInfoVo.builder()
                .streamKey(streamKey)
                .groups(xInfoGroups).build();
    }

    @Override
    public Map<String, Map<String, List<String>>> streamXPending(String streamKey) {
        Map<String, Map<String, List<String>>> map = new HashMap<>();
        StreamInfo.XInfoGroups groups = stringRedisTemplate.opsForStream().groups(streamKey);
        groups.forEach(group -> {
            StreamInfo.XInfoConsumers consumers = stringRedisTemplate.opsForStream().consumers(EasyLogConstants.STREAM_KEY, group.groupName());
            Map<String, List<String>> item = new HashMap<>();
            consumers.forEach(consumer -> {
                PendingMessages pending = stringRedisTemplate.opsForStream().pending(streamKey, Consumer.from(group.groupName(), consumer.consumerName()));
                Iterator<PendingMessage> iterator = pending.stream().iterator();
                List<String> ids = new ArrayList<>();
                while (iterator.hasNext()) {
                    PendingMessage next = iterator.next();
                    String value = next.getId().getValue();
                    ids.add(value);
                }
                item.put(consumer.consumerName(), ids);
            });
            map.put(group.groupName(), item);
        });
        return map;
    }

    @Override
    public Long streamXAck(String streamKey, String groupName, List<String> recordIds) {
        String[] ids = new String[recordIds.size()];
        recordIds.toArray(ids);
        return stringRedisTemplate.opsForStream().acknowledge(streamKey, groupName, ids);
    }
}
