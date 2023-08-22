package com.chj.easy.log.core.model;

import lombok.Builder;
import lombok.Data;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/21 17:34
 */
@Data
@Builder
public class LogAlarmPlatform {

    private String alarmPlatformName;

    private String accessToken;

    private String secret;

    private String status;
}