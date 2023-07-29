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
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * description RedisManager
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/20 17:12
 */
@Slf4j(topic = EasyLogConstants.EASY_LOG_TOPIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisManager {

    public static AtomicInteger RETRY_TIMES = new AtomicInteger(0);

    public static AtomicInteger JEDIS_CONNECTION_ERROR_TIMES = new AtomicInteger(0);

    private final static AtomicBoolean INIT_JEDIS_POOL_INITIALIZED = new AtomicBoolean(false);

    private final static AtomicBoolean SCHEDULE_PUSH_LOG_INITIALIZED = new AtomicBoolean(false);

    private static JedisPool JEDIS_POOL = null;

    public static void initJedisPool(String redisMode,
                                     String redisAddress,
                                     String redisPass,
                                     int redisDb,
                                     int redisPoolMaxIdle,
                                     int redisPoolMaxTotal,
                                     int redisConnectionTimeout) {
        if (INIT_JEDIS_POOL_INITIALIZED.compareAndSet(false, true)) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(redisPoolMaxIdle);
            config.setMaxTotal(redisPoolMaxTotal);
            config.setTestOnBorrow(true);
            if ("single".equals(redisMode)) {
                String[] arrays = redisAddress.split(":");
                JEDIS_POOL = new JedisPool(config, arrays[0], Integer.parseInt(arrays[1]), redisConnectionTimeout, redisPass, redisDb, "easy_log");
            }
        }
    }

    public static void schedulePushLog(BlockingQueue<LogTransferred> queue, int maxPushSize, long redisStreamMaxLen) {
        if (SCHEDULE_PUSH_LOG_INITIALIZED.compareAndSet(false, true)) {
            List<LogTransferred> logTransferredList = new ArrayList<>();
            EasyLogThreadPool.newEasyLogScheduledExecutorInstance().scheduleWithFixedDelay(() -> {
                if (logTransferredList.isEmpty()) {
                    queue.drainTo(logTransferredList, Math.min(queue.size(), maxPushSize));
                }
                if (logTransferredList.isEmpty()) {
                    return;
                }
                try (Jedis jedis = JEDIS_POOL.getResource()) {
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
}
