package com.chj.easy.log.admin.mqtt;

import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.common.enums.CmdTypeEnum;
import com.chj.easy.log.common.model.CmdUp;
import com.chj.easy.log.common.model.LoggerConfig;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttPublishMessage;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.core.server.event.IMqttMessageListener;
import net.dreamlu.iot.mqtt.spring.server.MqttServerTemplate;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.tio.core.ChannelContext;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/29 10:43
 */
@Slf4j
@Service
public class MqttServerMessageListener implements IMqttMessageListener, SmartInitializingSingleton {
    @Resource
    private ApplicationContext applicationContext;

    private MqttServerTemplate mqttServerTemplate;

    @Override
    public void onMessage(ChannelContext context, String clientId, String topic, MqttQoS qos, MqttPublishMessage message) {
        String msg = new String(message.payload(), StandardCharsets.UTF_8);
        log.info("context:{} clientId:{} message:{} payload:{}", context, clientId, message, msg);
        if (clientId.startsWith(EasyLogConstants.MQTT_CLIENT_ID_APP_PREFIX)) {
            if (topic.startsWith(EasyLogConstants.MQTT_CMD_UP_PREFIX)) {
                CmdUp cmdUp = JSONUtil.toBean(msg, CmdUp.class);
                String appName = cmdUp.getAppName();
                String namespace = cmdUp.getNamespace();
                String currIp = cmdUp.getCurrIp();
                CmdTypeEnum cmdType = cmdUp.getCmdType();
                if (CmdTypeEnum.GET_LOGGER_CONFIGURATIONS.equals(cmdType)) {
                    List<LoggerConfig> loggerConfigs = cmdUp.getLoggerConfigs();
                }
            }
        }
    }

    @Override
    public void afterSingletonsInstantiated() {
        // 单利 bean 初始化完成之后从 ApplicationContext 中获取 bean
        mqttServerTemplate = applicationContext.getBean(MqttServerTemplate.class);
    }
}
