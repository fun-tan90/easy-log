package com.chj.easy.log.admin.rest;


import com.chj.easy.log.core.convention.Res;
import com.chj.easy.log.core.model.SlidingWindow;
import com.chj.easy.log.core.service.EsService;
import com.chj.easy.log.core.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 11:08
 */
@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Resource
    EsService esService;

    @Resource
    RedisService redisService;

    @GetMapping("analyzer")
    public Res<List<String>> test(@RequestParam(defaultValue = "ik_smart", required = false) String analyzer, String content) {
        return Res.ok(esService.analyze(analyzer, content));
    }

    @GetMapping("sliding-window")
    public SlidingWindow slidingWindow() {
        redisService.slidingWindow("S_W:LOG_INPUT_SPEED:DEBUG", System.currentTimeMillis(), 15);
        redisService.slidingWindow("S_W:LOG_INPUT_SPEED:DEBUG", System.currentTimeMillis(), 15);
        redisService.slidingWindow("S_W:LOG_INPUT_SPEED:ERROR", System.currentTimeMillis(), 15);
        return redisService.slidingWindow("S_W:LOG_INPUT_SPEED:INFO", System.currentTimeMillis(), 15);
    }

    @GetMapping("sliding-window-count")
    public Map<String, Integer> slidingWindowCount() {
        return redisService.slidingWindowCount("S_W:LOG_INPUT_SPEED:");
    }
}
