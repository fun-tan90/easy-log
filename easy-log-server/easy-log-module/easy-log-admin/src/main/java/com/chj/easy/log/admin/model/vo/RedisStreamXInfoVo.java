package com.chj.easy.log.admin.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/19 8:31
 */
@Data
@Builder
public class RedisStreamXInfoVo {

    private String streamKey;

    private List<XInfoGroup> groups;

    @Data
    @Builder
    public static class XInfoGroup {

        private String groupName;

        private List<XInfoConsumer> consumers;
    }

    @Data
    @Builder
    public static class XInfoConsumer {

        private String consumerName;

        private long idleTimeMs;

        private long pendingCount;
    }
}
