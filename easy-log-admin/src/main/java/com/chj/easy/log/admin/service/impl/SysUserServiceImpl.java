package com.chj.easy.log.admin.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.chj.easy.log.admin.model.SysUserLoginCmd;
import com.chj.easy.log.admin.property.EasyLogAdminProperties;
import com.chj.easy.log.admin.service.SysCaptchaService;
import com.chj.easy.log.admin.service.SysUserService;
import com.chj.easy.log.core.convention.constants.AuthConstant;
import com.chj.easy.log.core.convention.enums.IErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/6/12 22:00
 */
@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    EasyLogAdminProperties easyLogAdminProperties;

    @Resource
    SysCaptchaService sysCaptchaService;

    @Override
    public String basicAuth(SysUserLoginCmd sysUserLoginCmd) {
        sysCaptchaService.validate(sysUserLoginCmd.getCaptchaKey(), sysUserLoginCmd.getCaptchaValue());
        String username = sysUserLoginCmd.getUsername();
        String password = sysUserLoginCmd.getPassword();
        if (!easyLogAdminProperties.getUsername().equals(username) || !easyLogAdminProperties.getPassword().equals(password)) {
            return IErrorCode.AUTH_1001001.getMessage();
        }
        StpUtil.login(username);
        return AuthConstant.TOKEN_PREFIX + StpUtil.getTokenValue();
    }
}
