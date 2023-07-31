package com.chj.easy.log.compute.listener;

import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.compute.property.EasyLogComputeProperties;
import com.chj.easy.log.compute.stream.RedisStreamComputeMessageListener;
import com.chj.easy.log.core.service.EsService;
import com.chj.easy.log.core.service.CacheService;
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
@Component
public class ComputeInitListener implements ApplicationListener<ApplicationReadyEvent> {

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    @Resource
    private EsService esService;

    @Resource
    private RedisStreamComputeMessageListener redisStreamComputeMessageListener;

    @Resource
    private CacheService cacheService;

    @Resource
    private EasyLogComputeProperties easyLogComputeProperties;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (initialized.compareAndSet(false, true)) {
            esService.initLifecyclePolicyAndTemplate();

            String streamKey = EasyLogConstants.REDIS_STREAM_KEY;
            String groupName = EasyLogConstants.GROUP_COMPUTE_NAME;
            String consumerNamePrefix = EasyLogConstants.CONSUMER_COMPUTE_NAME + "-";
            int[] consumerGlobalOrders = easyLogComputeProperties.getConsumerGlobalOrders();
            cacheService.initGroupAndConsumers(streamKey, groupName, consumerNamePrefix, consumerGlobalOrders, redisStreamComputeMessageListener);
        }
    }
}