package com.chj.easy.log.core.model.cmd.es;

import lombok.Data;

/**
 * description 高亮参数
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 23:25
 */
@Data
public class HighLightCmd {

    /**
     * 高亮字段截取长度,默认为100
     */
    private int fragmentSize;

    /**
     * 前置标签
     */
    private String preTag;

    /**
     * 后置标签
     */
    private String postTag;

    /**
     * 高亮字段列表
     */
    private String highLightField;

    /**
     * 高亮字段类型
     */
    private String highLightType;
}
