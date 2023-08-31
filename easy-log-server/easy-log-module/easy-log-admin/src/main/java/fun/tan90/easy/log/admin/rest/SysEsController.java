package fun.tan90.easy.log.admin.rest;

import fun.tan90.easy.log.core.convention.Res;
import fun.tan90.easy.log.core.convention.annotation.Log;
import fun.tan90.easy.log.core.model.IndexList;
import fun.tan90.easy.log.core.service.EsService;
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