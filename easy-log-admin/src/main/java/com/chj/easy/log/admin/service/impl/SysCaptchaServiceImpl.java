package com.chj.easy.log.admin.service.impl;

import com.chj.easy.log.admin.model.CaptchaGenerateCmd;
import com.chj.easy.log.admin.service.SysCaptchaService;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.convention.enums.IErrorCode;
import com.chj.easy.log.core.convention.exception.ClientException;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author chj
 * @date 2021年08月04日 8:49
 */
@Slf4j
@Service
public class SysCaptchaServiceImpl implements SysCaptchaService {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public String generateArithmeticCaptcha(CaptchaGenerateCmd captchaGenerateCmd) {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(captchaGenerateCmd.getWidth(), captchaGenerateCmd.getHeight());
        // 几位数运算，默认是两位
        captcha.setLen(captchaGenerateCmd.getLen());
        // 获取运算的结果：5
        String result = captcha.text();
        stringRedisTemplate.opsForValue().set(EasyLogConstants.CAPTCHA_IMG + captchaGenerateCmd.getCaptchaKey(), result, captchaGenerateCmd.getSurvival(), TimeUnit.MINUTES);
        return captcha.toBase64();
    }

    @Override
    public void validate(String captchaKey, String captchaValue) {
        String captchaRightValue = stringRedisTemplate.opsForValue().get(EasyLogConstants.CAPTCHA_IMG + captchaKey);
        if (captchaRightValue == null) {
            throw new ClientException(IErrorCode.AUTH_1001002);
        }
        try {
            if (!captchaRightValue.equalsIgnoreCase(captchaValue.trim())) {
                throw new ClientException(IErrorCode.AUTH_1001002);
            }
        } finally {
            stringRedisTemplate.delete(EasyLogConstants.CAPTCHA_IMG + captchaKey);
        }
    }
}
