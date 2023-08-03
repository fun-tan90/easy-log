package com.chj.easy.log.admin.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaTokenConsts;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import com.chj.easy.log.admin.model.cmd.SysUserLoginCmd;
import com.chj.easy.log.admin.model.vo.SysUserInfoVo;
import com.chj.easy.log.admin.model.vo.SysUserMqttVo;
import com.chj.easy.log.admin.property.EasyLogAdminProperties;
import com.chj.easy.log.admin.service.SysCaptchaService;
import com.chj.easy.log.admin.service.SysUserService;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.convention.enums.IErrorCode;
import com.chj.easy.log.core.convention.exception.ClientException;
import com.chj.easy.log.core.model.Topic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;

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
            throw new ClientException(IErrorCode.AUTH_1001001);
        }
        boolean rememberMe = sysUserLoginCmd.isRememberMe();
        SaLoginModel saLoginModel = new SaLoginModel();
        if (rememberMe) {
            saLoginModel.setTimeout(7 * 24 * 3600);
        }
        StpUtil.login(username, saLoginModel.setIsLastingCookie(rememberMe));
        String tokenValue = StpUtil.getTokenValue();

        SysUserInfoVo sysUserInfoVo = SysUserInfoVo.builder()
                .userId("1")
                .userName("管理员")
                .roles(StpUtil.getRoleList())
                .permissions(StpUtil.getPermissionList())
                .build();
        StpUtil.getTokenSession().set("mqttClientId", EasyLogConstants.MQTT_CLIENT_ID_FRONT_PREFIX + tokenValue);
        StpUtil.getTokenSession().set("mqttUserName", RandomUtil.randomString(6));
        StpUtil.getTokenSession().set("mqttPassword", RandomUtil.randomString(8));
        StpUtil.getSession().set("sysUserInfo", sysUserInfoVo);
        return StpUtil.getStpLogic().getConfigOrGlobal().getTokenPrefix() + SaTokenConsts.TOKEN_CONNECTOR_CHAT + tokenValue;
    }

    @Override
    public SysUserInfoVo userInfo() {
        if (!StpUtil.isLogin()) {
            throw new ClientException(IErrorCode.AUTH_1001005);
        }
        return StpUtil.getSession().getModel("sysUserInfo", SysUserInfoVo.class);
    }

    @Override
    public SysUserMqttVo userMqttInfo() {
        if (StpUtil.isLogin()) {
            String tokenValue = StpUtil.getTokenValue();
            String mqttClientId = EasyLogConstants.MQTT_CLIENT_ID_FRONT_PREFIX + tokenValue;
            String md5 = SecureUtil.md5(mqttClientId);
            return SysUserMqttVo
                    .builder()
                    .mqttBrokerUrl(easyLogAdminProperties.getMqttBrokerUrl())
                    .mqttClientId(mqttClientId)
                    .mqttUserName(md5.substring(0, 6))
                    .mqttPassword(md5.substring(6))
                    .subTopics(Collections.singletonList(
                            Topic.builder()
                                    .topic(EasyLogConstants.LOG_INPUT_SPEED_TOPIC)
                                    .qos(1)
                                    .build())
                    )
                    .build();
        }
        throw new ClientException(IErrorCode.AUTH_1001005);
    }
}
