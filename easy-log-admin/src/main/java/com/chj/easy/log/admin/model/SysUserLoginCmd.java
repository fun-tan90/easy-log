package com.chj.easy.log.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NotNull
    @NotBlank
    private String captchaKey;

    @NotNull
    @NotBlank
    private String captchaValue;
}
