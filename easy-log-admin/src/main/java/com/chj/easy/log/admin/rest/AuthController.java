package com.chj.easy.log.admin.rest;

import cn.dev33.satoken.stp.StpUtil;
import com.chj.easy.log.admin.model.cmd.CaptchaGenerateCmd;
import com.chj.easy.log.admin.model.cmd.SysUserLoginCmd;
import com.chj.easy.log.admin.model.vo.SysUserAuthVo;
import com.chj.easy.log.admin.service.SysCaptchaService;
import com.chj.easy.log.admin.service.SysUserService;
import com.chj.easy.log.core.convention.Res;
import com.chj.easy.log.core.convention.enums.IErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author chj
 * @date 2021年07月17日 16:38
 */
@Slf4j
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
    public Res<SysUserAuthVo> login(@RequestBody @Validated SysUserLoginCmd userLoginCmd) {
        return Res.ok(sysUserService.basicAuth(userLoginCmd));
    }

    @GetMapping("/isLogin")
    public Res<Boolean> isLogin() {
        String loginId = StpUtil.getLoginId().toString();
        log.info("=======LoginId==={}", loginId);
        return Res.ok(StpUtil.isLogin());
    }

    @GetMapping("/logout")
    public Res<Void> logout() {
        if (StpUtil.isLogin()) {
            StpUtil.logout();
            return Res.ok();
        }
        return Res.error(IErrorCode.AUTH_1001006);
    }
}