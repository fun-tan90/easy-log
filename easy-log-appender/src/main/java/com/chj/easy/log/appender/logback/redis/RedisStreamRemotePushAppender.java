package com.chj.easy.log.appender.logback.redis;


import com.chj.easy.log.appender.logback.AbstractRemotePushAppender;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.common.model.LogTransferred;
import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;
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
        if ("single" .equals(redisMode)) {
            // TODO redisAddress 正则校验
            String[] arrays = redisAddress.split(":");
            this.jedisPool = new JedisPool(config, arrays[0], Integer.parseInt(arrays[1]), redisConnectionTimeout, redisPass, redisDb);
        }
    }

    @Override
    protected void closeRemotePushClient() {
        if (!this.jedisPool.isClosed()) {
            this.jedisPool.close();
        }
    }

    @Override
    public void push(BlockingQueue<LogTransferred> queue, int maxPushSize) {
        long timeMillis = System.currentTimeMillis();
        List<LogTransferred> logTransferredList = new ArrayList<>(maxPushSize);
        queue.drainTo(logTransferredList, Math.min(queue.size(), maxPushSize));
        if (logTransferredList.isEmpty()) {
            return;
        }
        try (Jedis jedis = this.jedisPool.getResource();) {
            Pipeline pipelined = jedis.pipelined();
            logTransferredList.forEach(logTransferred -> {
                pipelined.xadd(EasyLogConstants.STREAM_KEY, StreamEntryID.NEW_ENTRY, logTransferred.toMap(), redisStreamMaxLen, false);
            });
            pipelined.sync();
        }
        System.out.println(Thread.currentThread().getName() + "->" + (System.currentTimeMillis() - timeMillis) + "ms" + "->" + logTransferredList.size());
    }
}