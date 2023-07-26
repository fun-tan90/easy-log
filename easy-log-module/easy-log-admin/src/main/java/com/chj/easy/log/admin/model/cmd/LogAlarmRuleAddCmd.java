package com.chj.easy.log.admin.model.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 用户登录
 *
 * @author 陈浩杰
 * @since 2023-06-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogAlarmRuleAddCmd {

    @NotNull
    @NotBlank
    private String appName;

    @NotNull
    @NotBlank
    private String appEnv;

    private String loggerName;

    @NotNull
    @NotEmpty
    private List<String> receiverList;

    @NotNull
    @NotBlank
    private String alarmPlatformType;

    @NotNull
    @NotBlank
    private String alarmPlatformId;

    @NotNull
    private Integer threshold;

    @NotNull
    private Integer period;

    private String status = "1";
}
