package com.chj.easy.log.admin.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/17 15:55
 */
@Data
@Builder
public class LogQueryBarChartVo {

    private String key;

    private long count;

    private List<BarDetail> barDetailList;

    @Data
    @AllArgsConstructor
    public static class BarDetail {

        private String subKey;

        private long subCount;
    }
}
