package fun.tan90.easy.log.admin.model.cmd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/21 17:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogAlarmPlatformAddCmd {

    @NotNull
    @NotBlank
    private String alarmPlatformType;

    @NotNull
    @NotBlank
    private String alarmPlatformName;

    @NotNull
    @NotBlank
    private String accessToken;

    private String secret;

    private String status = "1";
}