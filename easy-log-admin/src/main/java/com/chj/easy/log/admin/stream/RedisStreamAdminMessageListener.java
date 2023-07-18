package com.chj.easy.log.admin.stream;

import com.chj.easy.log.common.constant.EasyLogConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/16 8:40
 */
@Slf4j(topic = EasyLogConstants.LOG_TOPIC_ADMIN)
@Component
public class RedisStreamAdminMessageListener implements StreamListener<String, MapRecord<String, String, String>> {

    @Override
    public void onMessage(MapRecord<String, String, String> entries) {
        if (entries != null) {
            String recordId = entries.getId().getValue();
            Map<String, String> value = entries.getValue();
            log.info("recordId {} maps {}", recordId, value);
        }
    }
}
