package com.chj.easy.log.server.admin.rest;


import com.chj.easy.log.server.common.convention.Res;
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
    public Res<String> index() {
        log.error("hello error");
        log.warn("hello warn");
        log.info("hello info");
        log.debug("hello debug");
        return Res.ok("");
    }

    @GetMapping("error-log")
    public Res<String> errorLog() {
        log.error("hello error");
        return Res.ok("");
    }
}
