package com.chj.easy.log.admin.model.cmd;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/17 15:55
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogQueryBarChartCmd extends BaseLogQueryCmd {

    private String dateHistogramField = "dateTime";

    private String termsField = "level";

    @NotNull
    @NotEmpty
    private String calendarInterval = "1m";

    @NotNull
    @NotEmpty
    private String format = "yyyy-MM-dd HH:mm";
}
