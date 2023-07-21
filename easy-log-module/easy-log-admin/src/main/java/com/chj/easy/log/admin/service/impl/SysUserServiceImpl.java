package com.chj.easy.log.admin.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.chj.easy.log.admin.model.cmd.SysUserLoginCmd;
import com.chj.easy.log.admin.model.vo.SysUserAuthVo;
import com.chj.easy.log.admin.model.vo.SysUserMqttVo;
import com.chj.easy.log.admin.property.EasyLogAdminProperties;
import com.chj.easy.log.admin.service.SysCaptchaService;
import com.chj.easy.log.admin.service.SysUserService;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.convention.enums.IErrorCode;
import com.chj.easy.log.core.convention.exception.ClientException;
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
    public SysUserAuthVo basicAuth(SysUserLoginCmd sysUserLoginCmd) {
        sysCaptchaService.validate(sysUserLoginCmd.getCaptchaKey(), sysUserLoginCmd.getCaptchaValue());
        String username = sysUserLoginCmd.getUsername();
        String password = sysUserLoginCmd.getPassword();
        if (!easyLogAdminProperties.getUsername().equals(username) || !easyLogAdminProperties.getPassword().equals(password)) {
            throw new ClientException(IErrorCode.AUTH_1001001);
        }
        boolean rememberMe = sysUserLoginCmd.isRememberMe();
        SaLoginModel saLoginModel = new SaLoginModel();
        if (rememberMe) {
            saLoginModel.setTimeout(7 * 24 * 3600);
        }
        StpUtil.login(username, saLoginModel.setIsLastingCookie(rememberMe));
        String tokenValue = StpUtil.getTokenValue();

        StpUtil.getTokenSession().set("mqttClientId", tokenValue);
        StpUtil.getTokenSession().set("mqttUserName", RandomUtil.randomString(6));
        StpUtil.getTokenSession().set("mqttPassword", RandomUtil.randomString(8));
        StpUtil.getTokenSession().set("sysVersion", EasyLogConstants.EASY_LOG_VERSION);
        return SysUserAuthVo.builder()
                .token(tokenValue)
                .userId("1")
                .userName("管理员")
                .roles(StpUtil.getRoleList())
                .permissions(StpUtil.getPermissionList())
                .build();
    }

    @Override
    public SysUserMqttVo userMqttInfo() {
        if (StpUtil.isLogin()) {
            SaSession tokenSession = StpUtil.getTokenSession();
            return SysUserMqttVo
                    .builder()
                    .mqttClientId(tokenSession.getString("mqttClientId"))
                    .mqttUserName(tokenSession.getString("mqttUserName"))
                    .mqttUserName(tokenSession.getString("mqttPassword"))
                    .build();
        }
        throw new ClientException(IErrorCode.AUTH_1001006);
    }
}
