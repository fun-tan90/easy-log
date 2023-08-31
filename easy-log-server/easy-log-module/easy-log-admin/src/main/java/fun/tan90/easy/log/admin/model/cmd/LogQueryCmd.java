package fun.tan90.easy.log.admin.model.cmd;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/17 15:55
 */
@Data
public class LogQueryCmd {

    @NotNull
    private BaseLogQueryCmd baseParam;

    private PageParam pageParam;

    private BarChartParam barChartParam;

    @Data
    public static class PageParam {

        private Integer pageNum = 1;

        private Integer pageSize = 500;
    }

    @Data
    public static class BarChartParam {

        private String dateHistogramField = "@timestamp";

        private String termsField = "level";

        @NotNull
        @NotBlank
        private String calendarInterval = "1m";

        @NotNull
        @NotBlank
        private String format = "yyyy-MM-dd HH:mm";
    }
}
