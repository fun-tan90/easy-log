package fun.tan90.easy.log.common.model;

import fun.tan90.easy.log.common.enums.CmdTypeEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.logging.LogLevel;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/29 10:56
 */
@Data
@Builder
public class CmdDown {

    private CmdTypeEnum cmdType;

    private String loggerName;

    /**
     * null时，代表重置
     */
    private LogLevel logLevel;
}
