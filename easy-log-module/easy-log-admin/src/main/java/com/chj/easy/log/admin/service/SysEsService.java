package com.chj.easy.log.admin.service;


import com.chj.easy.log.admin.model.vo.EsIndexVo;
import com.chj.easy.log.admin.model.vo.RedisStreamXInfoVo;

import java.util.List;
import java.util.Map;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/6/12 22:00
 */
public interface SysEsService {

    List<EsIndexVo> indexList(String indexNamePattern);

    void barChart();
}
