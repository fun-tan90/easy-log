package com.chj.easy.log.core.property;

import lombok.Data;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/25 10:43
 */
@Data
public class IndexLifecyclePolicy {

    /**
     * 1m、1h
     */
    private String hotMaxAge = "5m";

    /**
     * 1mb、1gb
     */
    private String hotMaxPrimaryShardSize = "10gb";

    private long hotMaxDocs = 10000000;

    private String deleteMinAge = "1d";
}