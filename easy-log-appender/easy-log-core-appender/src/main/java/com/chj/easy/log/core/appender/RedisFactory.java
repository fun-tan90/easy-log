package com.chj.easy.log.core.appender;

import cn.hutool.core.lang.Singleton;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.common.model.LogTransferred;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/20 17:12
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisFactory {

    public static JedisPool getJedisPool(String redisMode,
                                         String redisAddress,
                                         String redisPass,
                                         int redisDb,
                                         int redisPoolMaxIdle,
                                         int redisPoolMaxTotal,
                                         int redisConnectionTimeout) {
        return Singleton.get("jedis_pool", () -> {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(redisPoolMaxIdle);
            config.setMaxTotal(redisPoolMaxTotal);
            config.setTestOnBorrow(true);
            if ("single".equals(redisMode)) {
                // TODO redisAddress 正则校验
                String[] arrays = redisAddress.split(":");
                return new JedisPool(config, arrays[0], Integer.parseInt(arrays[1]), redisConnectionTimeout, redisPass, redisDb);
            }
            return null;
        });
    }

    public static void schedulePush(BlockingQueue<LogTransferred> queue, JedisPool jedisPool, int maxPushSize, long redisStreamMaxLen) {
        EasyLogManager.EASY_LOG_SCHEDULED_EXECUTOR.scheduleWithFixedDelay(() -> {
            List<LogTransferred> logTransferredList = new ArrayList<>(maxPushSize);
            queue.drainTo(logTransferredList, Math.min(queue.size(), maxPushSize));
            if (logTransferredList.isEmpty()) {
                return;
            }
            long timeMillis = System.currentTimeMillis();
            try (Jedis jedis = jedisPool.getResource()) {
                Pipeline pipelined = jedis.pipelined();
                logTransferredList.forEach(logTransferred -> {
                    pipelined.xadd(EasyLogConstants.STREAM_KEY, StreamEntryID.NEW_ENTRY, logTransferred.toMap(), redisStreamMaxLen, false);
                });
                pipelined.sync();
            }
            System.out.println(Thread.currentThread().getName() + "->" + (System.currentTimeMillis() - timeMillis) + "ms" + "->" + logTransferredList.size());
        }, 5, 100, TimeUnit.MILLISECONDS);
    }

    public static boolean ping() {
        JedisPool jedisPool = Singleton.get("jedis_pool", () -> null);
        if (Objects.isNull(jedisPool)) {
            return false;
        }
        try (Jedis jedis = jedisPool.getResource()) {
            return "PONG".equals(jedis.ping());
        }
    }

    public static void closeJedisPool() {
        JedisPool jedisPool = Singleton.get("jedis_pool", () -> null);
        try {
            if (!Objects.isNull(jedisPool)) {
                jedisPool.close();
            }
        } finally {
            Singleton.remove("jedis_pool");
        }

    }
}
