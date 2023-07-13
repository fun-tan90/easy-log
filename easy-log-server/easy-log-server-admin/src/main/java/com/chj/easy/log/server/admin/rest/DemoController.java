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

    @GetMapping("info")
    public Res<String> info() {
        for (int i = 0; i < 100; i++) {
            log.error("hello error" + i);
            log.warn("hello warn" + i);
            log.info("hello info" + i);
            log.debug("hello debug" + i);
        }
        return Res.ok("info");
    }
}
