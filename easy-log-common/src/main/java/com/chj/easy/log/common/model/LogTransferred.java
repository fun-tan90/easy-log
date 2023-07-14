package com.chj.easy.log.common.model;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/12 22:24
 */
@Data
@Builder
public class LogTransferred {

    private Date date;

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

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("date", DateUtil.format(date, DatePattern.NORM_DATETIME_MS_PATTERN));
        map.put("appName", appName);
        map.put("appEnv", appEnv);
        map.put("level", level);
        map.put("loggerName", loggerName);
        map.put("threadName", threadName);
        map.put("traceId", Optional.ofNullable(traceId).orElse("-"));
        map.put("spanId", Optional.ofNullable(spanId).orElse("-"));
        map.put("currIp", Optional.ofNullable(currIp).orElse("-"));
        map.put("preIp", Optional.ofNullable(preIp).orElse("-"));
        map.put("method", Optional.ofNullable(method).orElse("-"));
        map.put("lineNumber", lineNumber);
        map.put("content", content);
        return map;
    }
}