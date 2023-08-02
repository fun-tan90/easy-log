package com.chj.easy.log.example.boot2.common.rest;


import com.yomahub.tlog.core.annotation.TLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 11:08
 */
@Slf4j
@RestController
public class DemoController {

    private static final ExecutorService NEW_FIXED_THREAD_POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @GetMapping
    @TLogAspect({"id"})
    public String index(String id) {
//        MDC.put("name","陈浩杰");
        NEW_FIXED_THREAD_POOL.execute(() -> {
            String threadName = Thread.currentThread().getName();
            log.error("{} error is {} ", threadName, id);
            log.warn("{} warn is {} ", threadName, id);
            log.info("{} info is {} ", threadName, id);
            log.debug("{} debug is {} ", threadName, id);
            log.trace("{} trace is {} ", threadName, id);
        });
        return id;
    }

    @GetMapping("test-error")
    public void error() {
        try {
            log.error("业务异常1");
            throw new RuntimeException("运行时异常1");
        } catch (Exception e) {
            log.error("捕获异常01", e);
            log.error("捕获异常02{}", e.getMessage());
            log.error("捕获异常03{}", e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @GetMapping("scheduled")
    public String scheduled(String id) {
        Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors())
                .scheduleAtFixedRate(() -> {
                    String threadName = Thread.currentThread().getName();
                    log.error("{} error is {} ", threadName, id);
                    log.warn("{} warn is {} ", threadName, id);
                    log.info("{} info is {} ", threadName, id);
                    log.debug("{} debug is {} ", threadName, id);
                    log.trace("{} trace is {} ", threadName, id);
                }, 1, 1, TimeUnit.MILLISECONDS);
        return "ok";
    }
}
