package fun.tan90.easy.log.collector.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 10:31
 */
@Data
@ConfigurationProperties(prefix = EasyLogCollectorProperties.PREFIX)
public class EasyLogCollectorProperties {

    public static final String PREFIX = "easy-log.collector";

    private boolean enable = false;

    private boolean banner = true;

    private int queueCapacity = 10000;

    private int insertBatchSize = 100;
}
