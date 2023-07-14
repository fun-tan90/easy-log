package com.chj.easy.log.server.common.model;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.easyes.annotation.IndexField;
import org.dromara.easyes.annotation.IndexId;
import org.dromara.easyes.annotation.IndexName;
import org.dromara.easyes.annotation.rely.FieldType;
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
@IndexName(replicasNum = 1, shardsNum = 6)
public class LogDoc {

    @IndexId(type = IdType.CUSTOMIZE)
    private String id;

    @IndexField(fieldType = FieldType.DATE, dateFormat = DatePattern.NORM_DATETIME_MS_PATTERN)
    private Date dateTime;

    @IndexField(fieldType = FieldType.KEYWORD)
    private String appName;

    @IndexField(fieldType = FieldType.KEYWORD)
    private String appEnv;

    @IndexField(fieldType = FieldType.KEYWORD)
    private String traceId;

    @IndexField(fieldType = FieldType.KEYWORD)
    private String spanId;

    @IndexField(fieldType = FieldType.KEYWORD)
    private String currIp;

    @IndexField(fieldType = FieldType.KEYWORD)
    private String preIp;

    @IndexField(fieldType = FieldType.KEYWORD)
    private String level;

    @IndexField(fieldType = FieldType.TEXT)
    private String loggerName;

    @IndexField(fieldType = FieldType.TEXT)
    private String threadName;

    @IndexField(fieldType = FieldType.TEXT)
    private String method;

    @IndexField(fieldType = FieldType.KEYWORD)
    private String lineNumber;

    @IndexField(fieldType = FieldType.TEXT)
    private String content;

    public static String indexName() {
        return EasyLogConstants.INDEX_FIXED_PREFIX + DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN);
    }

    public static String newIndexName() {
        return EasyLogConstants.INDEX_FIXED_PREFIX + DateUtil.format(DateUtil.offsetDay(new Date(), 1), DatePattern.NORM_DATE_PATTERN);
    }
}