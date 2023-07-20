package com.chj.easy.log.core.model.cmd.es;

import lombok.Data;


/**
 * description 自定义排序参数 通常由前端传入
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 23:25
 */
@Data
public class OrderByCmd {
    /**
     * 排序字段
     */
    private String order;
    /**
     * 排序规则 ASC:升序 DESC:降序
     */
    private String sort;
}
