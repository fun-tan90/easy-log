package com.chj.easy.log.appender.logback.redis;


import com.chj.easy.log.common.model.LogTransferred;
import com.chj.easy.log.appender.logback.AbstractRemotePushAppender;
import com.chj.easy.log.common.constant.EasyLogConstants;
import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.StreamEntryID;

import java.util.concurrent.BlockingQueue;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/12 18:18
 */
@Setter
@Getter
public class RedisStreamRemotePushAppender extends AbstractRemotePushAppender {

    private JedisPool jedisPool;

    /**
     * single、sentinel、cluster
     */
    private String redisMode = "single";

    private String redisAddress = "127.0.0.1:6379";

    private String redisPass;

    private int redisDb;

    private int redisConnectionTimeout = 1000;

    private long redisStreamMaxLen = 1000000;

    private int redisPoolMaxTotal = 30;

    private int redisPoolMaxIdle = 30;

    @Override
    public void initRemotePushClient() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisPoolMaxTotal);
        config.setMaxIdle(redisPoolMaxIdle);
        config.setTestOnBorrow(true);
        if ("single".equals(redisMode)) {
            // TODO redisAddress 正则校验
            String[] arrays = redisAddress.split(":");
            this.jedisPool = new JedisPool(config, arrays[0], Integer.parseInt(arrays[1]), redisConnectionTimeout, redisPass, redisDb);
        }
    }

    @Override
    public void push(BlockingQueue<LogTransferred> queue) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            while (queue.remainingCapacity() != -1) {
                LogTransferred logTransferred = queue.poll();
                if (logTransferred == null) {
                    break;
                }
                jedis.xadd(EasyLogConstants.STREAM_KEY, StreamEntryID.NEW_ENTRY, logTransferred.toMap(), redisStreamMaxLen, false);
            }
        }
    }
}