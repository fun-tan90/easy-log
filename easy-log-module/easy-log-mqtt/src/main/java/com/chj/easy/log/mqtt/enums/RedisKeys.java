package com.chj.easy.log.mqtt.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


/**
 * description redis key 汇总，方便统一处理
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/29 21:33
 */
@Getter
@RequiredArgsConstructor
public enum RedisKeys {

    /**
     * mqtt 服务端节点
     */
    SERVER_NODES("mqtt:server:nodes:"),

    /**
     * mqtt <-> redis pug/sub 集群内消息交互
     */
    REDIS_CHANNEL_EXCHANGE(RedisKeys.REDIS_CHANNEL_EXCHANGE_KEY),

    /**
     * 云端 -> 设备 redis pug/sub 下行数据通道，广播到 mqtt 集群
     */
    REDIS_CHANNEL_DOWN(RedisKeys.REDIS_CHANNEL_DOWN_KEY),

    /**
     * 连接状态存储
     */
    CONNECT_STATUS("mqtt:connect:status:"),

    /**
     * 遗嘱消息存储
     */
    MESSAGE_STORE_WILL("mqtt:messages:will:"),

    /**
     * 保留消息存储
     */
    MESSAGE_STORE_RETAIN("mqtt:messages:retain:"),
    ;

    private final String key;

    public static final String REDIS_CHANNEL_EXCHANGE_KEY = "mqtt:channel:exchange";

    public static final String REDIS_CHANNEL_DOWN_KEY = "mqtt:channel:down";

    /**
     * 用于拼接后缀
     *
     * @param suffix 后缀
     * @return 完整的 redis key
     */
    public String getKey(String suffix) {
        return this.key.concat(suffix);
    }
}
