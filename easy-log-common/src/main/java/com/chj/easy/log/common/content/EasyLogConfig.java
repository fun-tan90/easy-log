package com.chj.easy.log.common.content;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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

    private static final long serialVersionUID = -6541180061782004705L;

    private String appName = "default";

    private String namespace = "dev";

    private String mqttAddress = "127.0.0.1:1883";

    private String userName;

    private String password;

    private int queueSize = 10240;

    private int maxPushSize = 500;
}
