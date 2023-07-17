package com.chj.easy.log.admin.model.cmd;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/17 15:55
 */
@Data
public class LogQuerySelectCmd {

    @NotNull
    @NotEmpty
    private String date;

    @NotNull
    @NotEmpty
    private String appEnv;

    @NotNull
    @NotEmpty
    private List<String> selectKeys;
}
