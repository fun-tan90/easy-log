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
public class LogDropBoxCmd {

    @NotNull
    @NotBlank
    private String date;

    @NotNull
    @NotBlank
    private String appEnv;

    @NotNull
    @NotEmpty
    private List<String> selectKeys;

    private int size = 100;
}
