package fun.tan90.easy.log.admin.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/17 15:55
 */
@Data
@Builder
public class BarChartVo {

    private String key;

    private long count;

    private List<OneBarDetail> oneBarDetailList;

    @Data
    @AllArgsConstructor
    public static class OneBarDetail {

        private String key;

        private long count;
    }
}
