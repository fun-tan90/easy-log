package com.chj.easy.log.admin.rest;


import com.chj.easy.log.core.convention.Res;
import com.chj.easy.log.core.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
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
 * @date 2023/7/13 11:08
 */
@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {
}
