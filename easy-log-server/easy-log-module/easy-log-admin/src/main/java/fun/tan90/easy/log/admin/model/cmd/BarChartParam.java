package fun.tan90.easy.log.admin.model.cmd;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/10/17 17:56
 */
@Data
public class BarChartParam {

    private String dateHistogramField = "@timestamp";

    private String termsField = "level";

    @NotNull
    @NotBlank
    private String calendarInterval = "1m";

    @NotNull
    @NotBlank
    private String format = "yyyy-MM-dd HH:mm";
}