package com.chj.easy.log.example.boot2.common.rest;


import com.chj.easy.log.core.appender.annotation.EL;
import com.chj.easy.log.meter.annotation.Counter;
import com.yomahub.tlog.core.thread.TLogInheritableTask;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping("el")
    @EL(key = "userId", value = "#id")
    public String el(String id) {
        log.info("测试 MDC @EL");
        return id;
    }

    @GetMapping
    public String index(String id) {
        MDC.put("name", "陈浩杰");
        TLogInheritableTask tLogInheritableTask = new TLogInheritableTask() {
            @Override
            public void runTask() {
                String threadName = Thread.currentThread().getName();
                log.error("{} error is {} ", threadName, id);
                log.warn("{} warn is {} ", threadName, id);
                log.info("{} info is {} ", threadName, id);
                log.debug("{} debug is {} ", threadName, id);
                log.trace("{} trace is {} ", threadName, id);
            }
        };
        NEW_FIXED_THREAD_POOL.execute(tLogInheritableTask);
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


    @GetMapping("counter1")
    @Counter(description = "测试统计1")
    public String counter1(String name, HttpServletRequest request, MultipartFile file) {
        return "";
    }


    @GetMapping("counter2")
    @Counter(description = "测试统计2")
    public void counter2() {
    }
}
