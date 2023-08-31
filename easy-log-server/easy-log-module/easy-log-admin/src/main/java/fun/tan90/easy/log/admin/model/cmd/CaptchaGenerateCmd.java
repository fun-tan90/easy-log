package fun.tan90.easy.log.admin.model.cmd;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * description
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2022/9/8 22:55
 */
@Data
public class CaptchaGenerateCmd {

    /**
     * 验证码随机KEY,6位随机字符串
     */
    @NotNull
    @NotBlank
    @Length(min = 8, max = 12)
    private String captchaKey;

    private Integer width = 130;

    private Integer height = 48;

    private Integer len = 2;

    private Long survival = 5L;
}
