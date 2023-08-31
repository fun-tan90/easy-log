package fun.tan90.easy.log.admin.rest;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import fun.tan90.easy.log.admin.model.cmd.BaseLogQueryCmd;
import fun.tan90.easy.log.admin.model.cmd.LogDropBoxCmd;
import fun.tan90.easy.log.admin.model.cmd.LogQueryCmd;
import fun.tan90.easy.log.admin.model.vo.BarChartVo;
import fun.tan90.easy.log.admin.service.LogQueryService;
import fun.tan90.easy.log.core.convention.Res;
import fun.tan90.easy.log.core.convention.annotation.Log;
import fun.tan90.easy.log.core.convention.page.es.EsPageInfo;
import fun.tan90.easy.log.core.model.Doc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/19 8:42
 */
@Slf4j
@Log
@RestController
@RequestMapping("log-query")
public class LogQueryController {

    @Resource
    LogQueryService logQueryService;

    @PostMapping("/dsl")
    public Res<JSONObject> dsl(@RequestBody @Validated BaseLogQueryCmd baseLogQueryCmd) {
        return Res.ok(JSONUtil.parseObj(logQueryService.generateSearchSource(baseLogQueryCmd).toString()));
    }

    @PostMapping("/query-drop-box")
    public Res<Map<String, List<String>>> queryDropBox(@RequestBody @Validated LogDropBoxCmd logDropBoxCmd) {
        return Res.ok(logQueryService.queryDropBox(logDropBoxCmd));
    }

    @PostMapping("/paging")
    public Res<EsPageInfo<Doc>> paging(@RequestBody @Validated LogQueryCmd logQueryCmd) {
        return Res.ok(logQueryService.paging(logQueryCmd));
    }

    @PostMapping("/bar-chart")
    public Res<List<BarChartVo>> barChart(@RequestBody @Validated LogQueryCmd logQueryCmd) {
        return Res.ok(logQueryService.barChart(logQueryCmd));
    }
}