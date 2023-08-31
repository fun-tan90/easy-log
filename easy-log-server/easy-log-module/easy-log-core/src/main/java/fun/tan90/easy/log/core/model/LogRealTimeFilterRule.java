package fun.tan90.easy.log.core.model;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/21 17:34
 */
@Data
@Builder
public class LogRealTimeFilterRule {

    private String clientId;

    private String sql;

    private String namespace;

    private List<String> appNameList;

    private List<String> levelList;

    private String loggerName;

    private String lineNumber;

    private List<String> ipList;

    private String analyzer;

    private String content;

    private List<String> colList;

    private String whereCondition;
}