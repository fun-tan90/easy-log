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

    private String rolloverMaxAge = "5m";

    private String rolloverMaxSize = "10gb";

    private long rolloverMaxDocs = 10000000;

    private String deleteMinAge = "30d";
}