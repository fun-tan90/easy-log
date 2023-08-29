package com.chj.easy.log.collector.listener;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.common.model.LogTransferred;
import com.chj.easy.log.core.model.LogDoc;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttPublishMessage;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.core.client.IMqttClientMessageListener;
import net.dreamlu.iot.mqtt.spring.client.MqttClientSubscribe;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/12 14:24
 */
@Slf4j
@Component
@MqttClientSubscribe(value = "$share/collector/" + EasyLogConstants.MQTT_LOG_PREFIX + "/#", qos = MqttQoS.AT_LEAST_ONCE)
public class CollectorLogMessageListener implements IMqttClientMessageListener {

    @Resource
    private SimpleMeterRegistry simpleMeterRegistry;

    @Resource
    private BlockingQueue<LogDoc> logDocBlockingQueue;

    @Override
    public void onMessage(ChannelContext context, String topic, MqttPublishMessage message, byte[] payload) {
        String msg = new String(payload, StandardCharsets.UTF_8);
        log.debug("订阅到日志信息 topic:{} payload:{}", topic, msg);
        List<LogTransferred> logTransferredList = JSONUtil.toList(msg, LogTransferred.class);
        Counter counter = Counter.builder("collector.log.total.number")
                .description("日志收集总数量")
                .tag("appName", getAppName(topic))
                .tag("namespace", getNamespace(topic))
                .baseUnit("条").register(simpleMeterRegistry);
        counter.increment(logTransferredList.size());
        for (LogTransferred logTransferred : logTransferredList) {
            LogDoc logDoc = LogDoc.builder()
                    .timestamp(DateUtil.format(new Date(logTransferred.getTimestamp()), DatePattern.NORM_DATETIME_MS_PATTERN))
                    .appName(logTransferred.getAppName())
                    .namespace(logTransferred.getNamespace())
                    .seq(logTransferred.getSeq())
                    .level(logTransferred.getLevel())
                    .loggerName(logTransferred.getLoggerName())
                    .threadName(logTransferred.getThreadName())
                    .traceId(logTransferred.getTraceId())
                    .spanId(logTransferred.getSpanId())
                    .currIp(logTransferred.getCurrIp())
                    .preIp(logTransferred.getPreIp())
                    .method(logTransferred.getMethod())
                    .lineNumber(logTransferred.getLineNumber())
                    .content(logTransferred.getContent())
                    .mdc(logTransferred.getMdc())
                    .build();
            boolean offer = logDocBlockingQueue.offer(logDoc);
            if (!offer) {
                log.error("offer LogDoc failed");
            }
        }
    }

    private String getAppName(String topic) {
        return topic.split("/")[3];
    }

    private String getNamespace(String topic) {
        return topic.split("/")[2];
    }
}

