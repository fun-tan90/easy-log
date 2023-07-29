package com.chj.easy.log.admin.mqtt.listener;

import com.chj.easy.log.admin.mqtt.enums.RedisKeys;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.event.LogAlarmUnRegisterEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.core.server.MqttServerCreator;
import net.dreamlu.iot.mqtt.core.server.event.IMqttConnectStatusListener;
import net.dreamlu.mica.redis.cache.MicaRedisCache;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.tio.core.ChannelContext;


/**
 * description mqtt 连接监听，此处也可以添加发送到 mq 的逻辑，方便影子服务处理
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/29 21:34
 */
@Slf4j
@RequiredArgsConstructor
public class RedisMqttConnectStatusListener implements IMqttConnectStatusListener, SmartInitializingSingleton, DisposableBean {

    private final ApplicationContext applicationContext;

    private final MicaRedisCache redisCache;

    private MqttServerCreator serverCreator;

    @Override
    public void online(ChannelContext context, String clientId, String username) {
        redisCache.sAdd(getRedisKey(), clientId);
    }

    @Override
    public void offline(ChannelContext context, String clientId, String username, String reason) {
        redisCache.sRem(getRedisKey(), clientId);

        log.info("{} is offline", clientId);
        if (clientId.startsWith(EasyLogConstants.MQTT_CLIENT_ID_FRONT_PREFIX)) {
            applicationContext.publishEvent(new LogAlarmUnRegisterEvent(this, clientId));
        } else if (clientId.startsWith(EasyLogConstants.MQTT_CLIENT_ID_APP_PREFIX)) {

        }
    }

    /**
     * 设备上下线存储，key 的值为 前缀:nodeName
     *
     * @return redis key
     */
    private String getRedisKey() {
        return RedisKeys.CONNECT_STATUS.getKey(serverCreator.getNodeName());
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.serverCreator = applicationContext.getBean(MqttServerCreator.class);
    }

    @Override
    public void destroy() throws Exception {
        redisCache.del(getRedisKey());
    }
}
