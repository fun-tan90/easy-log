package fun.tan90.easy.log.admin.model.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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

    /**
     * 记住我天数
     */
    private long rememberMeDays = 7;

    @NotNull
    @NotBlank
    @Length(min = 8, max = 12)
    private String captchaKey;

    @NotNull
    @NotBlank
    private String captchaValue;
}
