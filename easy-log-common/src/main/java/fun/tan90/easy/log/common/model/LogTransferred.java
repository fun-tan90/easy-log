package fun.tan90.easy.log.common.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

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

    private long timestamp;

    private long seq;

    private String appName;

    private String namespace;

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

    private Map<String, String> mdc;
}