package com.chj.easy.log.server.collector.stream;

import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.server.common.model.LogDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Huhailong
 * @Description 监听消息
 * @Date 2021/3/10.
 */
@Slf4j
@RequiredArgsConstructor
public class RedisStreamMessageListener implements StreamListener<String, MapRecord<String, String, String>> {

    private final BlockingQueue<LogDoc> logDocBlockingQueue;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(MapRecord<String, String, String> entries) {
        if (entries != null) {
            String recordId = entries.getId().getValue();
            Map<String, String> value = entries.getValue();
            LogDoc logDoc = LogDoc.builder()
                    .id(recordId)
                    .timeStamp(Long.parseLong(value.get("timeStamp")))
                    .appName(value.get("appName"))
                    .appEnv(value.get("appEnv"))
                    .level(value.get("level"))
                    .loggerName(value.get("loggerName"))
                    .threadName(value.get("threadName"))
                    .traceId(value.get("traceId"))
                    .spanId(value.get("spanId"))
                    .currIp(value.get("currIp"))
                    .preIp(value.get("preIp"))
                    .method(value.get("method"))
                    .lineNumber(value.get("lineNumber"))
                    .content(value.get("content"))
                    .build();
            try {
                boolean offer = logDocBlockingQueue.offer(logDoc, 100, TimeUnit.MILLISECONDS);
                if (!offer) {
                    log.error("Dropping LogDoc due to timeout limit of [100] being exceeded");
                } else {
                    stringRedisTemplate.opsForStream().acknowledge(EasyLogConstants.STREAM_KEY, EasyLogConstants.GROUP_NAME, recordId);
                }
            } catch (InterruptedException e) {
                log.error("Interrupted while appending LogDoc to LogDocBlockingQueue", e);
            }
        }
    }

}
