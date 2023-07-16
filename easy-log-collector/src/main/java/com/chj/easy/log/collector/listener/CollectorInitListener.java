package com.chj.easy.log.collector.listener;

import cn.hutool.core.date.StopWatch;
import com.chj.easy.log.collector.property.EasyLogCollectorProperties;
import com.chj.easy.log.collector.stream.RedisStreamMessageListener;
import com.chj.easy.log.common.BannerPrint;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.model.Doc;
import com.chj.easy.log.core.model.LogDoc;
import com.chj.easy.log.core.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
@Slf4j(topic = EasyLogConstants.LOG_TOPIC)
@Component
public class CollectorInitListener implements ApplicationListener<ApplicationReadyEvent> {

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private BlockingQueue<LogDoc> logDocBlockingQueue;

    @Resource
    private EsService esService;

    @Resource
    private EasyLogCollectorProperties easyLogCollectorProperties;

    @Resource
    private RedisStreamMessageListener redisStreamMessageListener;

    @Resource
    private Environment environment;

    @Resource
    private StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (initialized.compareAndSet(false, true)) {
            Boolean enable = environment.getProperty("easy-log.admin.enable", Boolean.class);
            if (Boolean.FALSE.equals(enable)) {
                esService.createIndexIfNotExists(LogDoc.indexName());
            }

            createStreamKeyAndGroupAndConsumers();

            batchInsertLogDocBySchedule();

            if (easyLogCollectorProperties.isBanner()) {
                BannerPrint.printCollectorBanner();
            }
        }
    }

    private void batchInsertLogDocBySchedule() {
        EasyLogManager.EASY_LOG_SCHEDULED_EXECUTOR.scheduleWithFixedDelay(() -> {
            List<Doc> logDocs = new ArrayList<>();
            while (logDocBlockingQueue.remainingCapacity() != -1) {
                LogDoc logDoc = logDocBlockingQueue.poll();
                if (logDoc == null) {
                    break;
                }
                logDocs.add(logDoc);
                if (logDocs.size() == easyLogCollectorProperties.getInsertBatchSize()) {
                    break;
                }
            }
            if (!logDocs.isEmpty()) {
                StopWatch stopWatch = new StopWatch("es 批量输入");
                stopWatch.start("批量插入数据耗时");
                int insertedSize = esService.insertBatch(LogDoc.indexName(), logDocs);
                log.debug("批量输入条数[{}-{}]", logDocs.size(), insertedSize);
                stopWatch.stop();
                log.debug(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
            }
        }, 1, 100, TimeUnit.MILLISECONDS);
    }

    private void createStreamKeyAndGroupAndConsumers() {
        Boolean hasKey = stringRedisTemplate.hasKey(EasyLogConstants.STREAM_KEY);
        if (Boolean.FALSE.equals(hasKey)) {
            stringRedisTemplate.opsForStream().createGroup(EasyLogConstants.STREAM_KEY, EasyLogConstants.GROUP_NAME);
        }
        StreamInfo.XInfoGroups groups = stringRedisTemplate.opsForStream().groups(EasyLogConstants.STREAM_KEY);
        Optional<StreamInfo.XInfoGroup> xInfoGroupOpt = groups.stream().filter(n -> n.groupName().equals(EasyLogConstants.GROUP_NAME)).findAny();
        if (!xInfoGroupOpt.isPresent()) {
            stringRedisTemplate.opsForStream().createGroup(EasyLogConstants.STREAM_KEY, EasyLogConstants.GROUP_NAME);
        }
        for (int consumerGlobalOrder : easyLogCollectorProperties.getConsumerGlobalOrders()) {
            streamMessageListenerContainer
                    .receive(
                            Consumer.from(EasyLogConstants.GROUP_NAME, EasyLogConstants.GROUP_CONSUMER_NAME + "-" + consumerGlobalOrder),
                            StreamOffset.create(EasyLogConstants.STREAM_KEY, ReadOffset.lastConsumed()),
                            redisStreamMessageListener
                    );
        }
    }
}
