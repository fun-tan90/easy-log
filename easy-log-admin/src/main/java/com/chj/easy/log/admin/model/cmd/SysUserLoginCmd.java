package com.chj.easy.log.admin.model.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 用户登录
 *
 * @author 陈浩杰
 * @since 2023-06-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserLoginCmd {

    /**
     * 账户名
     */
    @NotNull
    @NotBlank
    private String username;

    /**
     * 密码
     */
    @NotNull
    @NotBlank
    private String password;

    /**
     * 记住我
     */
    private boolean rememberMe;

    @NotNull
    @NotBlank
    @Min(value = 8)
    private String captchaKey;

    @NotNull
    @NotBlank
    private String captchaValue;
}