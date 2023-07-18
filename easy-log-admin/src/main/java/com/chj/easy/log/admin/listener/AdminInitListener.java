package com.chj.easy.log.admin.listener;

import com.chj.easy.log.admin.property.EasyLogAdminProperties;
import com.chj.easy.log.admin.stream.RedisStreamAdminMessageListener;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.model.LogDoc;
import com.chj.easy.log.core.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 10:15
 */
@Slf4j
@Component
public class AdminInitListener implements ApplicationListener<ApplicationReadyEvent> {

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private EsService esService;

    @Resource
    private RedisStreamAdminMessageListener redisStreamAdminMessageListener;

    @Resource
    private StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer;

    @Resource
    private EasyLogAdminProperties easyLogAdminProperties;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (initialized.compareAndSet(false, true)) {
            esService.createIndexIfNotExists(LogDoc.indexName());

            createStreamKeyAndGroupAndConsumers();
        }
    }

    private void createStreamKeyAndGroupAndConsumers() {
        Boolean hasKey = stringRedisTemplate.hasKey(EasyLogConstants.STREAM_KEY);
        if (Boolean.FALSE.equals(hasKey)) {
            stringRedisTemplate.opsForStream().createGroup(EasyLogConstants.STREAM_KEY, EasyLogConstants.GROUP_COLLECTOR_NAME);
        }
        StreamInfo.XInfoGroups groups = stringRedisTemplate.opsForStream().groups(EasyLogConstants.STREAM_KEY);
        Optional<StreamInfo.XInfoGroup> xInfoGroupOpt = groups.stream().filter(n -> n.groupName().equals(EasyLogConstants.GROUP_COLLECTOR_NAME)).findAny();
        if (!xInfoGroupOpt.isPresent()) {
            stringRedisTemplate.opsForStream().createGroup(EasyLogConstants.STREAM_KEY, EasyLogConstants.GROUP_ADMIN_NAME);
        }
        for (int consumerGlobalOrder : easyLogAdminProperties.getConsumerGlobalOrders()) {
            streamMessageListenerContainer
                    .receive(
                            Consumer.from(EasyLogConstants.GROUP_ADMIN_NAME, EasyLogConstants.GROUP_ADMIN_CONSUMER_NAME + "-" + consumerGlobalOrder),
                            StreamOffset.create(EasyLogConstants.STREAM_KEY, ReadOffset.lastConsumed()),
                            redisStreamAdminMessageListener
                    );
        }
    }
}
