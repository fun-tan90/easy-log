package com.chj.easy.log.core.appender;

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
            } else if ("sentinel".equals(redisMode)) {

            } else if ("cluster".equals(redisMode)) {

            }
        }
    }

    public static void schedulePushLog(BlockingQueue<LogTransferred> blockingQueue, int maxPushSize, long redisStreamMaxLen) {
        if (SCHEDULE_PUSH_LOG_INITIALIZED.compareAndSet(false, true)) {
            if (Objects.isNull(JEDIS_POOL)) {
                return;
            }
            List<LogTransferred> logTransferredList = new ArrayList<>();
            EasyLogThreadPool.newEasyLogScheduledExecutorInstance().scheduleWithFixedDelay(() -> {
                if (logTransferredList.isEmpty()) {
                    blockingQueue.drainTo(logTransferredList, Math.min(blockingQueue.size(), maxPushSize));
                }
                if (logTransferredList.isEmpty()) {
                    return;
                }
                try (Jedis jedis = JEDIS_POOL.getResource()) {
                    Pipeline pipelined = jedis.pipelined();
                    logTransferredList.forEach(logTransferred -> {
                        pipelined.xadd(EasyLogConstants.REDIS_STREAM_KEY, StreamEntryID.NEW_ENTRY, logTransferred.toMap(), redisStreamMaxLen, true);
                    });
                    pipelined.sync();
                } catch (JedisConnectionException e) {
                    log.error("{}, {} logs are discarded", e.getMessage(), logTransferredList.size());
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException ignored) {
                    }
                } finally {
                    logTransferredList.clear();
                }
            }, 5, 50, TimeUnit.MILLISECONDS);
        }
    }
}