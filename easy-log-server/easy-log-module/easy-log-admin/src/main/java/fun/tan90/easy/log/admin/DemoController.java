package fun.tan90.easy.log.admin;


import fun.tan90.easy.log.core.convention.Res;
import fun.tan90.easy.log.core.model.SlidingWindow;
import fun.tan90.easy.log.core.service.CacheService;
import fun.tan90.easy.log.core.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LoggerConfiguration;
import org.springframework.boot.logging.LoggingSystem;
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
    LoggingSystem loggingSystem;

    @Resource
    EsService esService;

    @Resource
    CacheService cacheService;

    @GetMapping("analyzer")
    public Res<List<String>> test(@RequestParam(defaultValue = "ik_smart", required = false) String analyzer, String content) {
        return Res.ok(esService.analyze(analyzer, content));
    }

    @GetMapping("sliding-window")
    public SlidingWindow slidingWindow() {
        cacheService.slidingWindow("S_W:LOG_INPUT_SPEED:DEBUG", String.valueOf(System.currentTimeMillis()), System.currentTimeMillis(), 15);
        cacheService.slidingWindow("S_W:LOG_INPUT_SPEED:DEBUG", String.valueOf(System.currentTimeMillis()), System.currentTimeMillis(), 15);
        cacheService.slidingWindow("S_W:LOG_INPUT_SPEED:ERROR", String.valueOf(System.currentTimeMillis()), System.currentTimeMillis(), 15);
        return cacheService.slidingWindow("S_W:LOG_INPUT_SPEED:INFO", String.valueOf(System.currentTimeMillis()), System.currentTimeMillis(), 15);
    }

    @GetMapping("sliding-window-count")
    public Map<String, Integer> slidingWindowCount() {
        return cacheService.slidingWindowCount("S_W:LOG_INPUT_SPEED:");
    }

    @GetMapping
    public String index() {
        List<LoggerConfiguration> loggerConfigurations = loggingSystem.getLoggerConfigurations();
        System.out.println("loggerConfigurations = " + loggerConfigurations);
        return "ok";
    }
}
