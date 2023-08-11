package com.chj.easy.log.core.model;

import cn.hutool.core.annotation.Alias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

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

    @Alias("@timestamp")
    private String timestamp;

    private String appName;

    private String namespace;

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

    private Map<String, String> mdc;

    public static String indexName() {
        return "easy-log-ds";
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