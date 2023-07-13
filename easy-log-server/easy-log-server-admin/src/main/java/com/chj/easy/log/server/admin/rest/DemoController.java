package com.chj.easy.log.server.admin.rest;

import com.chj.easy.log.server.admin.convention.Res;
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

    @GetMapping("info")
    public Res<String> info() {
        log.error("hello error");
        log.warn("hello warn");
        log.debug("hello debug");
        log.info("hello info");
        log.trace("hello trace");
        return Res.ok("info");
    }
}
