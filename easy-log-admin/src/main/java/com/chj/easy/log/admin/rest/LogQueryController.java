package com.chj.easy.log.admin.rest;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.admin.model.cmd.BaseLogQueryCmd;
import com.chj.easy.log.admin.model.cmd.LogQueryPageCmd;
import com.chj.easy.log.admin.model.cmd.LogQuerySelectCmd;
import com.chj.easy.log.admin.service.LogQueryService;
import com.chj.easy.log.core.convention.Res;
import com.chj.easy.log.core.convention.page.es.EsPageInfo;
import com.chj.easy.log.core.model.Doc;
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
 * @author chj
 * @date 2021年07月17日 16:38
 */
@Slf4j
@RestController
@RequestMapping("log-query")
public class LogQueryController {

    @Resource
    LogQueryService logQueryService;

    @PostMapping("/dsl")
    public Res<JSONObject> dsl(@RequestBody @Validated BaseLogQueryCmd baseLogQueryCmd) {
        return Res.ok(JSONUtil.parseObj(logQueryService.generateSearchSource(baseLogQueryCmd).toString()));
    }

    @PostMapping("/query-select")
    public Res<Map<String, List<String>>> querySelect(@RequestBody @Validated LogQuerySelectCmd logQuerySelectCmd) {
        return Res.ok(logQueryService.querySelect(logQuerySelectCmd));
    }

    @PostMapping("/paging")
    public Res<EsPageInfo<Doc>> paging(@RequestBody @Validated LogQueryPageCmd logQueryPageCmd) {
        return Res.ok(logQueryService.paging(logQueryPageCmd));
    }
}