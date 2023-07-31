package com.chj.easy.log.example.boot2.common.rest;


import com.yomahub.tlog.core.annotation.TLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executors;

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

    @GetMapping
    @TLogAspect({"id"})
    public String index(String id) {
//        MDC.put("name","陈浩杰");
        for (int i = 0; i < 12; i++) {
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()).execute(() -> {
                String threadName = Thread.currentThread().getName();
                log.error(threadName + "error is {} ", id);
                log.warn(threadName + "warn is {} ", id);
                log.info(threadName + "info is {} ", id);
                log.debug(threadName + "debug is {} ", id);
            });
        }
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

    @GetMapping("log-level")
    public String logLevel() {
        log.error("error");
        log.warn("warn");
        log.info("info");
        log.debug("debug");
        log.trace("trace");
        return "ok";
    }
}