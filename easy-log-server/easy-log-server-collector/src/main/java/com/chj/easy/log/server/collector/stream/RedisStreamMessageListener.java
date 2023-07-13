package com.chj.easy.log.server.collector.stream;

import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.server.common.model.LogDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;

import java.util.Map;

/**
 * @author Huhailong
 * @Description 监听消息
 * @Date 2021/3/10.
 */
@Slf4j
@RequiredArgsConstructor
public class RedisStreamMessageListener implements StreamListener<String, MapRecord<String, String, String>> {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(MapRecord<String, String, String> entries) {
        if (entries != null) {
            Map<String, String> value = entries.getValue();
            LogDoc logDoc = LogDoc.builder()
                    .id(entries.getId().getValue())
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
            stringRedisTemplate.opsForStream().acknowledge(EasyLogConstants.STREAM_KEY, EasyLogConstants.GROUP_NAME, entries.getId().getValue());
            System.out.format("线程名称:%s\n%s\n", Thread.currentThread().getName(), JSONUtil.toJsonPrettyStr(logDoc));
        }
    }

}
