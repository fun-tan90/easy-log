package com.chj.easy.log.example.boot2.rest;


import com.chj.easy.log.common.EasyLogManager;
import com.yomahub.tlog.core.annotation.TLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
            EasyLogManager.EASY_LOG_FIXED_THREAD_POOL.execute(() -> {
                log.error("error is {} {}", id, Thread.currentThread().getName());
                log.warn("warn is {} {}", id, Thread.currentThread().getName());
                log.info("info is {} {}", id, Thread.currentThread().getName());
                log.debug("debug is {} {}", id, Thread.currentThread().getName());
            });
        }
        return id;
    }
}
