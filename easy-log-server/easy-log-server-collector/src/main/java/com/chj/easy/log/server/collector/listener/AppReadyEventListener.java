package com.chj.easy.log.server.collector.listener;

import cn.hutool.core.date.StopWatch;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.server.collector.property.EasyLogCollectorProperties;
import com.chj.easy.log.server.common.model.LogDoc;
import com.chj.easy.log.server.common.service.EsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.connection.stream.StreamInfo;
import org.springframework.data.redis.core.StringRedisTemplate;

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
@Slf4j
@RequiredArgsConstructor
public class AppReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private final StringRedisTemplate stringRedisTemplate;

    private final BlockingQueue<LogDoc> logDocBlockingQueue;

    private final EsService esService;

    private final EasyLogCollectorProperties easyLogCollectorProperties;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (initialized.compareAndSet(false, true)) {
            createStreamKeyAndGroup();

            createLogDocIndex();

            batchInsertLogDocBySchedule();
        }
    }

    private void createLogDocIndex() {
        esService.createIndex(LogDoc.indexName(), EasyLogConstants.EASY_LOG_INDEX_MAPPINGS);
    }

    private void batchInsertLogDocBySchedule() {
        EasyLogManager.EASY_LOG_SCHEDULED_EXECUTOR.scheduleWithFixedDelay(() -> {
            List<LogDoc> logDocs = new ArrayList<>();
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
                // TODO 批量插入
                log.info("es 批量输入条数【{}】", 0);
                stopWatch.stop();
                log.info(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
            }
        }, 1, 100, TimeUnit.MILLISECONDS);
    }

    private void createStreamKeyAndGroup() {
        Boolean hasKey = stringRedisTemplate.hasKey(EasyLogConstants.STREAM_KEY);
        if (Boolean.FALSE.equals(hasKey)) {
            stringRedisTemplate.opsForStream().createGroup(EasyLogConstants.STREAM_KEY, EasyLogConstants.GROUP_NAME);
        } else {
            StreamInfo.XInfoGroups groups = stringRedisTemplate.opsForStream().groups(EasyLogConstants.STREAM_KEY);
            Optional<StreamInfo.XInfoGroup> xInfoGroupOpt = groups.stream().filter(n -> n.groupName().equals(EasyLogConstants.GROUP_NAME)).findAny();
            if (!xInfoGroupOpt.isPresent()) {
                stringRedisTemplate.opsForStream().createGroup(EasyLogConstants.STREAM_KEY, EasyLogConstants.GROUP_NAME);
            }
        }
    }
}
