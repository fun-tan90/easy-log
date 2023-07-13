package com.chj.easy.log.appender.logback;


import com.chj.easy.log.appender.LogTransferred;
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

    private String redisHost = "127.0.0.1";

    private int redisPort = 6379;

    private String redisPass;

    private int redisDb;

    private int redisConnectionTimeout = 1000;

    private long redisStreamMaxLen = 1000000;

    @Override
    void initRemotePushClient() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(30);
        config.setMaxIdle(10);
        config.setTestOnBorrow(true);
        this.jedisPool = new JedisPool(config, redisHost, redisPort, redisConnectionTimeout, redisPass, redisDb);
    }

    @Override
    void push(BlockingQueue<LogTransferred> queue) {
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