package com.chj.easy.log.admin.model.cmd;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/17 15:55
 */
@Data
public class BaseLogQueryCmd {

    @NotNull
    @NotBlank
    private String appEnv;

    private List<String> appNameList;

    private List<String> levelList;

    private String traceId;

    private String loggerName;

    private String lineNumber;

    private List<String> ipList;

    private String content;

    private List<String> ascList;

    private List<String> descList;

    @NotNull
    @NotEmpty
    private String startDateTime;

    @NotNull
    @NotEmpty
    private String endDateTime;
}
