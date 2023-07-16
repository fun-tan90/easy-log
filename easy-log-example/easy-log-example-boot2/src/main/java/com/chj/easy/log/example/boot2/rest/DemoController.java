package com.chj.easy.log.example.boot2.rest;


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

        log.error("error is {}", id);
        log.warn("warn is {}", id);
        log.info("warn is {}", id);
        log.debug("debug is {}", id);

        log.error("hello error");
        log.warn("hello warn");
        log.info("hello info");
        log.debug("hello debug");
        return "";
    }
}
