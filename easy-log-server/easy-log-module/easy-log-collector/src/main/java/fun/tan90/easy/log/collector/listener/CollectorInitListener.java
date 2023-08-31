package fun.tan90.easy.log.collector.listener;

import fun.tan90.easy.log.collector.property.EasyLogCollectorProperties;
import fun.tan90.easy.log.common.threadpool.EasyLogThreadPool;
import fun.tan90.easy.log.core.convention.exception.ServiceException;
import fun.tan90.easy.log.core.model.Doc;
import fun.tan90.easy.log.core.model.LogDoc;
import fun.tan90.easy.log.core.service.EsService;
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
    private BlockingQueue<LogDoc> logDocBlockingQueue;

    @Resource
    private EsService esService;

    @Resource
    private EasyLogCollectorProperties easyLogCollectorProperties;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (initialized.compareAndSet(false, true)) {
            esService.initLifecyclePolicyAndTemplate();

            batchInsertLogDocBySchedule();
        }
    }

    private void batchInsertLogDocBySchedule() {
        List<Doc> logDocs = new ArrayList<>();
        EasyLogThreadPool.newEasyLogScheduledExecutorInstance().scheduleWithFixedDelay(() -> {
            logDocBlockingQueue.drainTo(logDocs, Math.min(logDocBlockingQueue.size(), easyLogCollectorProperties.getInsertBatchSize()));
            if (!logDocs.isEmpty()) {
                try {
                    long start = System.currentTimeMillis();
                    int insertedSize = esService.insertBatch(LogDoc.indexName(), logDocs, true);
                    log.debug("批量输入条数[{}-{}]，耗时{}ms，Queue remaining capacity:{}", logDocs.size(), insertedSize, (System.currentTimeMillis() - start), logDocBlockingQueue.remainingCapacity());
                } catch (ServiceException e) {
                    log.error("es 批量插入异常", e);
                } finally {
                    logDocs.clear();
                }
            }
        }, 1, 50, TimeUnit.MILLISECONDS);
    }
}
