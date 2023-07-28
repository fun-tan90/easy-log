package com.chj.easy.log.core.appender.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/28 21:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppBasicInfo {

    private String appName;

    private String namespace;
}
