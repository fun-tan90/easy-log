package com.chj.easy.log.admin.rest;

import com.chj.easy.log.core.convention.Res;
import com.chj.easy.log.core.convention.annotation.Log;
import com.chj.easy.log.core.model.IndexList;
import com.chj.easy.log.core.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


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
@RequestMapping("sys/es")
public class SysEsController {

    @Resource
    EsService esService;

    @GetMapping("index")
    public Res<List<IndexList>> indexList(@RequestParam(defaultValue = "easy-log-*", required = false) String indexNamePattern) {
        return Res.ok(esService.indexList(indexNamePattern));
    }
}