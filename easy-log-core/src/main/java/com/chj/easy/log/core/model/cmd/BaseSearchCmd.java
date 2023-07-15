package com.chj.easy.log.core.model.cmd;


import com.chj.easy.log.core.model.cmd.es.HighLightCmd;
import com.chj.easy.log.core.model.cmd.es.OrderByCmd;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 23:27
 */
@Data
public class BaseSearchCmd {

    /**
     * 当前页
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 当前主类的高亮字段列表
     */
    private List<HighLightCmd> highLightCmdList = new ArrayList<>();

    /**
     * 排序规则
     */
    private List<OrderByCmd> orderByCmdList = new ArrayList<>();
}