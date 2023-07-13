package com.chj.easy.log.server.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.easyes.annotation.IndexField;
import org.dromara.easyes.annotation.IndexId;
import org.dromara.easyes.annotation.IndexName;
import org.dromara.easyes.annotation.rely.Analyzer;
import org.dromara.easyes.annotation.rely.FieldType;
import org.dromara.easyes.annotation.rely.IdType;

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
@IndexName(value = "easy_log", keepGlobalPrefix = true)
public class LogDoc {

    @IndexId(type = IdType.CUSTOMIZE)
    private String id;

    private long timeStamp;

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

    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.STANDARD, searchAnalyzer = Analyzer.STANDARD)
    private String content;
}