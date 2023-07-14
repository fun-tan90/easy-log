package com.chj.easy.log.server.common.model;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.easyes.annotation.IndexId;
import org.dromara.easyes.annotation.IndexName;
import org.dromara.easyes.annotation.rely.IdType;

import java.util.Date;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/12 22:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IndexName(value = "easy-log", keepGlobalPrefix = true)
public class LogDoc {

    @IndexId(type = IdType.CUSTOMIZE)
    private String id;

    private String date;

    private String appName;

    private String appEnv;

    private String level;

    private String loggerName;

    private String threadName;

    private String traceId;

    private String spanId;

    private String currIp;

    private String preIp;

    private String method;

    private String lineNumber;

    private String content;

    public static String indexName() {
        return "daily-easy-log-" + DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN);
    }

    public static String newIndexName() {
        return "daily-easy-log-" + DateUtil.format(DateUtil.offsetDay(new Date(), 1), DatePattern.NORM_DATE_PATTERN);
    }
}