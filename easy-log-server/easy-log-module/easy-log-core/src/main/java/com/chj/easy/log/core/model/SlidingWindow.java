package com.chj.easy.log.core.model;

import lombok.Builder;
import lombok.Data;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/20 10:49
 */
@Data
@Builder
public class SlidingWindow {

    private Long windowStart;

    private Long windowEnd;

    private Integer windowCount;
}
