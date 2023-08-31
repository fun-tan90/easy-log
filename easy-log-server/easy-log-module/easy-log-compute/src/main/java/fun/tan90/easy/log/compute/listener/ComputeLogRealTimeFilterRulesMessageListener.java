package fun.tan90.easy.log.compute.listener;

import cn.hutool.json.JSONUtil;
import fun.tan90.easy.log.common.constant.EasyLogConstants;
import fun.tan90.easy.log.compute.LogRealTimeFilterRulesManager;
import fun.tan90.easy.log.core.model.LogRealTimeFilterRule;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttPublishMessage;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.core.client.IMqttClientMessageListener;
import net.dreamlu.iot.mqtt.spring.client.MqttClientSubscribe;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;

import java.nio.charset.StandardCharsets;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/12 14:24
 */
@Slf4j
@Component
@MqttClientSubscribe(value = EasyLogConstants.LOG_REAL_TIME_FILTER_RULES_TOPIC + "+", qos = MqttQoS.EXACTLY_ONCE)
public class ComputeLogRealTimeFilterRulesMessageListener implements IMqttClientMessageListener {

    @Override
    public void onMessage(ChannelContext context, String topic, MqttPublishMessage message, byte[] payload) {
        String msg = new String(payload, StandardCharsets.UTF_8);
        log.debug("订阅到日志过滤规则信息 topic:{} payload:{}", topic, msg);
        if (topic.endsWith("put")) {
            LogRealTimeFilterRulesManager.putLogRealTimeFilterRule(JSONUtil.toBean(msg, LogRealTimeFilterRule.class));
        } else if (topic.endsWith("remove")) {
            LogRealTimeFilterRulesManager.removeLogRealTimeFilterRule(msg);
        }
    }
}

