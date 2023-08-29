package com.chj.easy.log.admin.rest;

import cn.dev33.satoken.stp.StpUtil;
import com.chj.easy.log.admin.model.cmd.CaptchaGenerateCmd;
import com.chj.easy.log.admin.model.cmd.SysUserLoginCmd;
import com.chj.easy.log.admin.model.vo.SysUserInfoVo;
import com.chj.easy.log.admin.model.vo.SysUserMqttVo;
import com.chj.easy.log.admin.service.SysCaptchaService;
import com.chj.easy.log.admin.service.SysUserService;
import com.chj.easy.log.core.convention.Res;
import com.chj.easy.log.core.convention.annotation.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/31 17:52
 */
@Slf4j
@Log
@RestController
public class AuthController {

    @Resource
    SysUserService sysUserService;

    @Resource
    SysCaptchaService sysCaptchaService;

    /**
     * 获取验证码
     *
     * @param captchaGenerateCmd
     * @return
     */
    @PostMapping("/captcha")
    public Res<String> captcha(@RequestBody @Validated CaptchaGenerateCmd captchaGenerateCmd) {
        return Res.ok(sysCaptchaService.generateArithmeticCaptcha(captchaGenerateCmd));
    }

    @PostMapping("/login")
    public Res<String> login(@RequestBody @Validated SysUserLoginCmd userLoginCmd) {
        return Res.ok(sysUserService.basicAuth(userLoginCmd));
    }

    @GetMapping("/user-info")
    public Res<SysUserInfoVo> userInfo() {
        return Res.ok(sysUserService.userInfo());
    }

    @GetMapping("/user-mqtt-info")
    public Res<SysUserMqttVo> userMqttInfo() {
        return Res.ok(sysUserService.userMqttInfo());
    }

    @GetMapping("/is-login")
    public Res<Boolean> isLogin() {
        return Res.ok(StpUtil.isLogin());
    }

    @GetMapping("/logout")
    public Res<Void> logout() {
        StpUtil.logout();
        return Res.ok();
    }
}