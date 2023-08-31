package fun.tan90.easy.log.common.model;

import lombok.Builder;
import lombok.Data;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/12 22:24
 */
@Data
@Builder
public class LoggerConfig {

    private String loggerName;

    private String configuredLevel;

    private String effectiveLevel;
}