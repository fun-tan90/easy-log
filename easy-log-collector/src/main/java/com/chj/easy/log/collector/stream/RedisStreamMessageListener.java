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

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/16 8:40
 */
@Slf4j(topic = EasyLogConstants.LOG_TOPIC)
public class RedisStreamMessageListener implements StreamListener<String, MapRecord<String, String, String>> {

    @Resource
    private BlockingQueue<LogDoc> logDocBlockingQueue;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(MapRecord<String, String, String> entries) {
        if (entries != null) {
            String recordId = entries.getId().getValue();
            Map<String, String> value = entries.getValue();
            LogDoc logDoc = LogDoc.builder()
                    .id(recordId)
                    .dateTime(DateUtil.format(new Date(Long.parseLong(value.get("timeStamp"))), DatePattern.NORM_DATETIME_MS_PATTERN))
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
                    .mdc(JSONUtil.parseObj(value.get("mdc")))
                    .build();
            try {
                boolean offer = logDocBlockingQueue.offer(logDoc, 100, TimeUnit.MILLISECONDS);
                if (!offer) {
                    log.error("Dropping LogDoc due to timeout limit of [100] being exceeded");
                }
            } catch (InterruptedException e) {
                log.error("Interrupted while appending LogDoc to LogDocBlockingQueue", e);
            } finally {
                stringRedisTemplate.opsForStream().acknowledge(EasyLogConstants.STREAM_KEY, EasyLogConstants.GROUP_NAME, recordId);
            }
        }
    }
}
