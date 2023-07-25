package com.chj.easy.log.core.model;

import cn.hutool.core.annotation.Alias;
import lombok.Builder;
import lombok.Data;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/17 15:55
 */
@Data
@Builder
public class IndexList {

    private String health;

    private String status;

    private String index;

    private String uuid;

    private String pri;

    private String rep;

    @Alias("docs.count")
    private String docsCount;

    @Alias("docs.deleted")
    private String docsDeleted;

    @Alias("store.size")
    private String storeSize;

    @Alias("pri.store.size")
    private String priStoreSize;
}