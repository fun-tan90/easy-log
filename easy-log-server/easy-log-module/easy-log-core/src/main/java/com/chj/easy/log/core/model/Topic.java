package com.chj.easy.log.core.model;

import lombok.Builder;
import lombok.Data;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/26 22:42
 */
@Data
@Builder
public class Topic {

    private String topic;

    private int qos;
}