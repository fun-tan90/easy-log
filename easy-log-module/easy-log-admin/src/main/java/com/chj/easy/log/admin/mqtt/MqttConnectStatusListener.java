package com.chj.easy.log.admin.mqtt;

import com.chj.easy.log.admin.register.LogRealTimeFilterRegistry;
import com.chj.easy.log.common.constant.EasyLogConstants;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.core.server.event.IMqttConnectStatusListener;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;

import javax.annotation.Resource;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/18 16:55
 */
@Slf4j(topic = EasyLogConstants.LOG_TOPIC_ADMIN)
@Component
public class MqttConnectStatusListener implements IMqttConnectStatusListener {

    @Resource
    LogRealTimeFilterRegistry logRealTimeFilterRegistry;

    @Override
    public void online(ChannelContext context, String clientId, String username) {
    }

    @Override
    public void offline(ChannelContext context, String clientId, String username, String reason) {
        logRealTimeFilterRegistry.unRegister(clientId);
    }
}
