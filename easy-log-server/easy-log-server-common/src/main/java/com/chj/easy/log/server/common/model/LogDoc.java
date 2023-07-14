package com.chj.easy.log.server.common.model;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import lombok.*;

import java.util.Date;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/12 22:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public abstract class LogDoc extends AbstractDoc {

    private transient String id;

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

    public static String indexName() {
        return EasyLogConstants.INDEX_FIXED_PREFIX + DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN);
    }

    public static String newIndexName() {
        return EasyLogConstants.INDEX_FIXED_PREFIX + DateUtil.format(DateUtil.offsetDay(new Date(), 1), DatePattern.NORM_DATE_PATTERN);
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