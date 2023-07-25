package com.chj.easy.log.core.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/21 17:34
 */
@Data
@Builder
public class LogAlarmContent {

    private Long windowStart;

    private Long windowEnd;

    private String ruleId;

    private String appName;

    private String appEnv;

    private String loggerName;

    private List<String> receiverList;

    private List<String> alarmPlatformId;

    private Integer threshold;

    private Integer period;
}