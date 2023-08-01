package com.chj.easy.log.collector.stream;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.model.LogDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/16 8:40
 */
@Slf4j(topic = EasyLogConstants.EASY_LOG_TOPIC)
@Component
public class RedisStreamCollectorMessageListener implements StreamListener<String, MapRecord<String, String, byte[]>> {

    @Resource
    private BlockingQueue<LogDoc> logDocBlockingQueue;

    @Override
    public void onMessage(MapRecord<String, String, byte[]> entries) {
        if (entries != null) {
            String recordId = entries.getId().getValue();
            Map<String, byte[]> value = entries.getValue();
            LogDoc logDoc = LogDoc.builder()
                    .id(recordId)
                    .timestamp(DateUtil.format(new Date(Long.parseLong(new String(value.get("timeStamp")))), DatePattern.NORM_DATETIME_MS_PATTERN))
                    .appName(new String(value.get("appName")))
                    .namespace(new String(value.get("namespace")))
                    .level(new String(value.get("level")))
                    .loggerName(new String(value.get("loggerName")))
                    .threadName(new String(value.get("threadName")))
                    .traceId(new String(value.get("traceId")))
                    .spanId(new String(value.get("spanId")))
                    .currIp(new String(value.get("currIp")))
                    .preIp(new String(value.get("preIp")))
                    .method(new String(value.get("method")))
                    .lineNumber(new String(value.get("lineNumber")))
                    .content(new String(value.get("content")))
                    .mdc(JSONUtil.parseObj(new String(value.get("mdc"))))
                    .build();
            boolean offer = logDocBlockingQueue.offer(logDoc);
            if (!offer) {
                log.error("offer LogDoc failed");
            }
        }
    }
}
