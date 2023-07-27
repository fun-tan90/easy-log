package com.chj.easy.log.core.appender;

import cn.hutool.core.lang.Singleton;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.common.model.LogTransferred;
import com.chj.easy.log.common.threadpool.EasyLogThreadPool;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * description RedisManager
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/20 17:12
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisManager {

    public static AtomicInteger RETRY_TIMES = new AtomicInteger(0);

    public static AtomicInteger JEDIS_CONNECTION_ERROR_TIMES = new AtomicInteger(0);

    public static JedisPool initJedisPool(String redisMode,
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
                return new JedisPool(config, arrays[0], Integer.parseInt(arrays[1]), redisConnectionTimeout, redisPass, redisDb, "easy_log");
            }
            // TODO sentinel cluster
            return null;
        });
    }

    public static void schedulePush(BlockingQueue<LogTransferred> queue, JedisPool jedisPool, int maxPushSize, long redisStreamMaxLen) {
        List<LogTransferred> logTransferredList = new ArrayList<>();
        EasyLogThreadPool.EASY_LOG_SCHEDULED_EXECUTOR.scheduleWithFixedDelay(() -> {
            if (logTransferredList.isEmpty()) {
                queue.drainTo(logTransferredList, Math.min(queue.size(), maxPushSize));
            }
            if (logTransferredList.isEmpty()) {
                return;
            }
            try (Jedis jedis = jedisPool.getResource()) {
                try {
                    Pipeline pipelined = jedis.pipelined();
                    logTransferredList.forEach(logTransferred -> {
                        pipelined.xadd(EasyLogConstants.REDIS_STREAM_KEY, StreamEntryID.NEW_ENTRY, logTransferred.toMap(), redisStreamMaxLen, false);
                    });
                    pipelined.sync();
                    RETRY_TIMES.set(0);
                    logTransferredList.clear();
                } catch (JedisConnectionException e) {
                    if (RETRY_TIMES.getAndIncrement() >= 3) {
                        RETRY_TIMES.set(0);
                        log.error("{}, {} logs are discarded", e.getMessage(), logTransferredList.size());
                        logTransferredList.clear();
                    }
                }
            } catch (JedisConnectionException e) {
                if (JEDIS_CONNECTION_ERROR_TIMES.getAndIncrement() > 100) {
                    throw e;
                }
                log.error(e.getMessage());
            }
        }, 5, 50, TimeUnit.MILLISECONDS);
    }
}
