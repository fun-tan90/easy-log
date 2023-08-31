package fun.tan90.easy.log.app.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chj
 * @date 2021年07月17日 16:38
 */
@Slf4j
@Controller
public class IndexController {

    @Value("${easy-log.admin.enable:true}")
    private boolean adminEnable;

    @Value("${easy-log.compute.enable:true}")
    private boolean computeEnable;

    @Value("${easy-log.collector.enable:true}")
    private boolean collectorEnable;

    @Resource
    private BuildProperties buildProperties;

    @GetMapping
    public String index(Model model) {
        List<String> modules = Arrays.asList(computeEnable ? "日志计算模块" : "", collectorEnable ? "日志收集模块" : "");
        model.addAttribute("modules", modules.stream().filter(StringUtils::hasLength).collect(Collectors.joining("和")));
        model.addAttribute("version", buildProperties.getVersion());
        return adminEnable ? "index" : "notify";
    }
}