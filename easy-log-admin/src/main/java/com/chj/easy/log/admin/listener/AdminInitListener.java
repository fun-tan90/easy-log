package com.chj.easy.log.admin.listener;

import com.chj.easy.log.admin.property.EasyLogAdminProperties;
import com.chj.easy.log.admin.stream.RedisStreamAdminMessageListener;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.model.LogDoc;
import com.chj.easy.log.core.service.EsService;
import com.chj.easy.log.core.service.RedisStreamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
    private EsService esService;

    @Resource
    private RedisStreamAdminMessageListener redisStreamAdminMessageListener;

    @Resource
    private RedisStreamService redisStreamService;

    @Resource
    private EasyLogAdminProperties easyLogAdminProperties;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (initialized.compareAndSet(false, true)) {
            esService.createIndexIfNotExists(LogDoc.indexName());

            String streamKey = EasyLogConstants.STREAM_KEY;
            String groupName = EasyLogConstants.GROUP_ADMIN_NAME;
            String consumerNamePrefix = EasyLogConstants.GROUP_ADMIN_CONSUMER_NAME + "-";
            int[] consumerGlobalOrders = easyLogAdminProperties.getConsumerGlobalOrders();
            redisStreamService.initStream(streamKey, groupName, consumerNamePrefix, consumerGlobalOrders, redisStreamAdminMessageListener);
        }
    }
}
