package com.chj.easy.log.mqtt.service.impl;

import com.chj.easy.log.mqtt.enums.RedisKeys;
import com.chj.easy.log.mqtt.model.ServerNode;
import com.chj.easy.log.mqtt.service.IMqttBrokerService;
import net.dreamlu.mica.core.utils.StringPool;
import net.dreamlu.mica.redis.cache.MicaRedisCache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * description mqtt broker 服务
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/29 21:33
 */
@Service
public class MqttBrokerServiceImpl implements IMqttBrokerService {
    @Resource
    MicaRedisCache redisCache;

    @Override
    public List<ServerNode> getNodes() {
        Set<String> nodeKeySet = redisCache.scan(RedisKeys.SERVER_NODES.getKey(StringPool.STAR));
        if (nodeKeySet.isEmpty()) {
            return Collections.emptyList();
        }
        int beginIndex = RedisKeys.SERVER_NODES.getKey().length();
        List<ServerNode> list = new ArrayList<>();
        for (String nodeKey : nodeKeySet) {
            String nodeName = nodeKey.substring(beginIndex);
            list.add(new ServerNode(nodeName, redisCache.get(nodeKey)));
        }
        return list;
    }

    @Override
    public long getOnlineClientSize() {
        Set<String> keySet = redisCache.scan(RedisKeys.CONNECT_STATUS.getKey(StringPool.STAR));
        if (keySet.isEmpty()) {
            return 0L;
        }
        long result = 0;
        for (String redisKey : keySet) {
            Long count = redisCache.getSetOps().size(redisKey);
            if (count != null) {
                result += count;
            }
        }
        return result;
    }

    @Override
    public List<String> getOnlineClients() {
        Set<String> keySet = redisCache.scan(RedisKeys.CONNECT_STATUS.getKey(StringPool.STAR));
        if (keySet.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> clientList = new ArrayList<>();
        for (String redisKey : keySet) {
            Set<String> members = redisCache.sMembers(redisKey);
            if (members != null && !members.isEmpty()) {
                clientList.addAll(members);
            }
        }
        return clientList;
    }

    @Override
    public long getOnlineClientSize(String nodeName) {
        Long count = redisCache.getSetOps().size(RedisKeys.CONNECT_STATUS.getKey(nodeName));
        return count == null ? 0L : count;
    }

    @Override
    public List<String> getOnlineClients(String nodeName) {
        Set<String> members = redisCache.sMembers(RedisKeys.CONNECT_STATUS.getKey(nodeName));
        if (members == null || members.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(members);
    }

}
