package com.chj.easy.log.core.model;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class LogDoc implements Doc {

    private static final String INDEX_FIXED_PREFIX = "daily-easy-log-";

    private String id;

    private String dateTime;

    private String appName;

    private String appEnv;

    private String traceId;

    private String spanId;

    private String currIp;

    private String preIp;

    private String level;

    private String loggerName;

    private String threadName;

    private String method;

    private String lineNumber;

    private String content;

    private JSONObject mdc;

    public static String indexName() {
        return INDEX_FIXED_PREFIX + DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN);
    }

    public static String indexName(String date) {
        return INDEX_FIXED_PREFIX + date;
    }

    public static String newIndexName() {
        return INDEX_FIXED_PREFIX + DateUtil.format(DateUtil.offsetDay(new Date(), 1), DatePattern.NORM_DATE_PATTERN);
    }

    @Override
    public void setIndexId(String id) {
        setId(id);
    }

    @Override
    public String indexId() {
        return this.id;
    }
}