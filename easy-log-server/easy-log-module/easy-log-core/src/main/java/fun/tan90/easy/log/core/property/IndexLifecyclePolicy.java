package fun.tan90.easy.log.core.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/25 10:43
 */
@Data
@ConfigurationProperties(prefix = EasyLogEsProperties.PREFIX + ".index-lifecycle-policy")
public class IndexLifecyclePolicy {

    /**
     * 热区最大保留时长
     */
    private String hotMaxAge = "1h";

    /**
     * 分片最大存储
     */
    private String hotMaxPrimaryShardSize = "10gb";

    /**
     * 文档最大数
     */
    private long hotMaxDocs = 10000000;

    /**
     * 索引存在时长
     */
    private String deleteMinAge = "60d";
}