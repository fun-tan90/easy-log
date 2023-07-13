package com.chj.easy.log.server.collector.stream;

import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.server.collector.event.LogDocPoolEvent;
import com.chj.easy.log.server.common.model.LogDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Huhailong
 * @Description 监听消息
 * @Date 2021/3/10.
 */
@Slf4j
@RequiredArgsConstructor
public class RedisStreamMessageListener implements StreamListener<String, MapRecord<String, String, String>> {

    private final ApplicationContext applicationContext;

    private final StringRedisTemplate stringRedisTemplate;

    private final static Map<String, List<LogDoc>> POOL = new ConcurrentHashMap<>();

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

            String poolKey = Thread.currentThread().getName();
            List<LogDoc> logDocs = POOL.get(poolKey);
            if (logDocs == null) {
                logDocs = new ArrayList<>();
                logDocs.add(logDoc);
                POOL.put(poolKey, logDocs);
            } else {
                logDocs.add(logDoc);
            }
            if (logDocs.size() == 2) {
                List<LogDoc> poolLogDoc = POOL.remove(poolKey);
                applicationContext.publishEvent(new LogDocPoolEvent(this, poolLogDoc));
                System.out.format("线程名称:%s\n%s\n", poolKey, JSONUtil.toJsonPrettyStr(poolLogDoc));
            }
        }
    }

}
