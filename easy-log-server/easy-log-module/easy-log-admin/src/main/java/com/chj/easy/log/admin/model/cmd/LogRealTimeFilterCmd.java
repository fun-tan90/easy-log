package com.chj.easy.log.admin.model.cmd;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/17 15:55
 */
@Data
public class LogRealTimeFilterCmd {

    @NotNull
    @NotBlank
    private String mqttClientId;

    @NotNull
    @NotBlank
    private String namespace;

    private List<String> appNameList;

    private List<String> levelList;

    private String loggerName;

    private String lineNumber;

    private List<String> ipList;

    private String analyzer = "ik_max_word";

    private String content;

    private List<String> colList = Collections.singletonList("*");

    private String whereCondition;
}
