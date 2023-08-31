package fun.tan90.easy.log.common.model;

import fun.tan90.easy.log.common.enums.CmdTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/29 10:56
 */
@Data
@Builder
public class CmdUp {

    private CmdTypeEnum cmdType;

    private String appName;

    private String namespace;

    private String currIp;

    private List<LoggerConfig> loggerConfigs;
}
