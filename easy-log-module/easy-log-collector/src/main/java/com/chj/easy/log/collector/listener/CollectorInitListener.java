package com.chj.easy.log.collector.listener;

import cn.hutool.core.date.StopWatch;
import com.chj.easy.log.collector.property.EasyLogCollectorProperties;
import com.chj.easy.log.collector.stream.RedisStreamCollectorMessageListener;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.convention.exception.ServiceException;
import com.chj.easy.log.core.model.Doc;
import com.chj.easy.log.core.model.LogDoc;
import com.chj.easy.log.core.service.EsService;
import com.chj.easy.log.core.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
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
public class CollectorInitListener implements ApplicationListener<ApplicationReadyEvent> {

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    @Resource
    private RedisService redisService;

    @Resource
    private BlockingQueue<LogDoc> logDocBlockingQueue;

    @Resource
    private EsService esService;

    @Resource
    private EasyLogCollectorProperties easyLogCollectorProperties;

    @Resource
    private RedisStreamCollectorMessageListener redisStreamCollectorMessageListener;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (initialized.compareAndSet(false, true)) {
            esService.initLifecyclePolicyAndTemplate();

            String streamKey = EasyLogConstants.REDIS_STREAM_KEY;
            String groupName = EasyLogConstants.GROUP_COLLECTOR_NAME;
            String consumerNamePrefix = EasyLogConstants.CONSUMER_COLLECTOR_NAME + "-";
            int[] consumerGlobalOrders = easyLogCollectorProperties.getConsumerGlobalOrders();
            redisService.initGroupAndConsumers(streamKey, groupName, consumerNamePrefix, consumerGlobalOrders, redisStreamCollectorMessageListener);

            batchInsertLogDocBySchedule();
        }
    }

    private void batchInsertLogDocBySchedule() {
        List<Doc> logDocs = new ArrayList<>();
        EasyLogManager.EASY_LOG_SCHEDULED_EXECUTOR.scheduleWithFixedDelay(() -> {
            logDocBlockingQueue.drainTo(logDocs, Math.min(logDocBlockingQueue.size(), easyLogCollectorProperties.getInsertBatchSize()));
            if (!logDocs.isEmpty()) {
                try {
                    StopWatch stopWatch = new StopWatch("es批量输入");
                    stopWatch.start();
                    int insertedSize = esService.insertBatch(LogDoc.indexName(), logDocs, true);
                    log.debug("批量输入条数[{}-{}]", logDocs.size(), insertedSize);
                    logDocs.clear();
                    stopWatch.stop();
                    log.debug("{}耗时:{}ms", stopWatch.getId(), stopWatch.getTotalTimeMillis());
                } catch (ServiceException e) {
                    log.error("es 批量插入异常", e);
                }
            }
        }, 1, 50, TimeUnit.MILLISECONDS);
    }
}
