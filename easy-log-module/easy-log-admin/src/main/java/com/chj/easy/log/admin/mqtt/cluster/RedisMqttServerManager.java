package com.chj.easy.log.admin.mqtt.cluster;

import com.chj.easy.log.admin.mqtt.enums.RedisKeys;
import net.dreamlu.iot.mqtt.core.server.MqttServer;
import net.dreamlu.iot.mqtt.core.server.MqttServerCreator;
import net.dreamlu.mica.core.utils.CharPool;
import net.dreamlu.mica.core.utils.INetUtil;
import net.dreamlu.mica.redis.cache.MicaRedisCache;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;

/**
 * mqtt 服务节点管理
 *
 * @author L.cm
 */
public class RedisMqttServerManager implements SmartInitializingSingleton, DisposableBean {

    private final MicaRedisCache redisCache;

    private final String nodeName;

    private final String hostName;

    public RedisMqttServerManager(MicaRedisCache redisCache, MqttServer mqttServer) {
        this.redisCache = redisCache;
        this.nodeName = mqttServer.getServerCreator().getNodeName();
        this.hostName = getHostName(mqttServer.getServerCreator());
    }

    @Override
    public void afterSingletonsInstantiated() {
        redisCache.set(RedisKeys.SERVER_NODES.getKey(nodeName), hostName);
    }

    @Override
    public void destroy() throws Exception {
        redisCache.del(RedisKeys.SERVER_NODES.getKey(nodeName));
    }

    private static String getHostName(MqttServerCreator mqttServerCreator) {
        return INetUtil.getHostIp() + CharPool.COLON + mqttServerCreator.getPort();
    }

}
