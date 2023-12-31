package fun.tan90.easy.log.admin.service.impl;

import fun.tan90.easy.log.admin.model.cmd.CaptchaGenerateCmd;
import fun.tan90.easy.log.admin.property.EasyLogAdminProperties;
import fun.tan90.easy.log.admin.service.SysCaptchaService;
import fun.tan90.easy.log.common.constant.EasyLogConstants;
import fun.tan90.easy.log.core.convention.enums.IErrorCode;
import fun.tan90.easy.log.core.convention.exception.ClientException;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
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
    EasyLogAdminProperties easyLogAdminProperties;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public String generateArithmeticCaptcha(CaptchaGenerateCmd captchaGenerateCmd) {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(captchaGenerateCmd.getWidth(), captchaGenerateCmd.getHeight());
        // 几位数运算，默认是两位
        captcha.setLen(captchaGenerateCmd.getLen());
        // 获取运算的结果：5
        String result = captcha.text();
        log.debug("验证码值为 {}", captcha.getArithmeticString());
        stringRedisTemplate.opsForValue().set(EasyLogConstants.CAPTCHA_IMG + captchaGenerateCmd.getCaptchaKey(), result, captchaGenerateCmd.getSurvival(), TimeUnit.MINUTES);
        return captcha.toBase64();
    }

    @Override
    public void validate(String captchaKey, String captchaValue) {
        if (!easyLogAdminProperties.isValidateCaptcha()) {
            return;
        }
        String captchaRightValue = stringRedisTemplate.opsForValue().get(EasyLogConstants.CAPTCHA_IMG + captchaKey);
        if (captchaRightValue == null) {
            throw new ClientException(IErrorCode.AUTH_1001002);
        }
        try {
            if (!captchaRightValue.equalsIgnoreCase(captchaValue.trim())) {
                throw new ClientException(IErrorCode.AUTH_1001000);
            }
        } finally {
            stringRedisTemplate.delete(EasyLogConstants.CAPTCHA_IMG + captchaKey);
        }
    }
}
