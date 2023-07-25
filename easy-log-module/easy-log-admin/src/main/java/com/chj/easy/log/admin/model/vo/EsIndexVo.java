package com.chj.easy.log.admin.model.vo;

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
public class EsIndexVo {

    private String health;

    private String status;

    private String index;

    private String uuid;

    private String pri;

    private String rep;

    private String docsCount;

    private String docsDeleted;

    private String storeSize;

    private String priStoreSize;

    private String dataStream;
}
