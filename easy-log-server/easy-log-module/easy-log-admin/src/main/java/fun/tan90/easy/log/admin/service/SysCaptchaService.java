package fun.tan90.easy.log.admin.service;


import fun.tan90.easy.log.admin.model.cmd.CaptchaGenerateCmd;

/**
 * @author chj
 * @date 2021年07月30日 11:30
 */
public interface SysCaptchaService {

    String generateArithmeticCaptcha(CaptchaGenerateCmd captchaGenerateCmd);

    void validate(String captchaKey, String captchaValue);
}
