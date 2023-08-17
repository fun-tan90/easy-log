package com.chj.easy.log.common.content;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/6/29 12:42
 */
@Setter
@Getter
public class EasyLogConfig implements Serializable {

    private static final long serialVersionUID = -6541180161782004725L;

    private String appName = "default";

    private String namespace = "default";

    private String mqttAddress = "tcp://127.0.0.1:1883";

    private String mqttUserName;

    private String mqttPassword;

    private List<Topic> topics = Collections.singletonList(Topic.builder().topicPattern("el/cmd/down/#").qos(2).build());

    private int queueSize = 10240;

    private int maxPushSize = 500;

    private boolean enableMeter;

    @Data
    @Builder
    public static class Topic {

        private String topicPattern;

        private int qos;
    }
}
