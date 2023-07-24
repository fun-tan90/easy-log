package com.chj.easy.log.compute.listener;

import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.compute.property.EasyLogComputeProperties;
import com.chj.easy.log.compute.stream.RedisStreamComputeMessageListener;
import com.chj.easy.log.core.service.EsService;
import com.chj.easy.log.core.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
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
public class ComputeInitListener implements ApplicationListener<ApplicationReadyEvent> {

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    @Resource
    private EsService esService;

    @Resource
    private RedisStreamComputeMessageListener redisStreamComputeMessageListener;

    @Resource
    private RedisService redisService;

    @Resource
    private EasyLogComputeProperties easyLogComputeProperties;

    @Resource
    private Environment environment;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (initialized.compareAndSet(false, true)) {
            Boolean enable = environment.getProperty("easy-log.admin.enable", Boolean.class);
            if (Boolean.FALSE.equals(enable)) {
                esService.initIndex();
            }

            String streamKey = EasyLogConstants.REDIS_STREAM_KEY;
            String groupName = EasyLogConstants.GROUP_COMPUTE_NAME;
            String consumerNamePrefix = EasyLogConstants.CONSUMER_COMPUTE_NAME + "-";
            int[] consumerGlobalOrders = easyLogComputeProperties.getConsumerGlobalOrders();
            redisService.initGroupAndConsumers(streamKey, groupName, consumerNamePrefix, consumerGlobalOrders, redisStreamComputeMessageListener);
        }
    }
}