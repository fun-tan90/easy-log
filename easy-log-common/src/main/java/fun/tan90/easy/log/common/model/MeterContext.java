package fun.tan90.easy.log.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/6/28 20:45
 */
@Data
@Builder
public class MeterContext implements Serializable {

    private long timeStamp;

    private String appName;

    private String namespace;

    private String currIp;

    private List<Meter> meters;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Meter implements Serializable {

        private String name;

        private String type;

        private Map<String, String> tags;

        private Double measurement;

        private String baseUnit;

        private String description;
    }
}
