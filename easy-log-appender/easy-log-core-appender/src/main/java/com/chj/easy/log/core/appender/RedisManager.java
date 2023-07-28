package com.chj.easy.log.core.appender;

import cn.hutool.core.lang.Singleton;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.common.model.LogTransferred;
import com.chj.easy.log.common.model.LoggerConfig;
import com.chj.easy.log.common.threadpool.EasyLogThreadPool;
import com.chj.easy.log.core.appender.model.AppBasicInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LoggerConfiguration;
import org.springframework.boot.logging.LoggingSystem;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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


    public static JedisPool initJedisPool(String redisMode,
                                          String redisAddress,
                                          String redisPass,
                                          int redisDb,
                                          int redisPoolMaxIdle,
                                          int redisPoolMaxTotal,
                                          int redisConnectionTimeout) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(redisPoolMaxIdle);
        config.setMaxTotal(redisPoolMaxTotal);
        config.setTestOnBorrow(true);
        JedisPool jedisPool = null;
        if ("single".equals(redisMode)) {
            String[] arrays = redisAddress.split(":");
            jedisPool = new JedisPool(config, arrays[0], Integer.parseInt(arrays[1]), redisConnectionTimeout, redisPass, redisDb, "easy_log");
        }
        if (!Objects.isNull(jedisPool)) {
            Singleton.put(jedisPool);
        }
        return jedisPool;
    }

    public static void schedulePushLog(BlockingQueue<LogTransferred> queue, JedisPool jedisPool, int maxPushSize, long redisStreamMaxLen) {
        List<LogTransferred> logTransferredList = new ArrayList<>();
        EasyLogThreadPool.newEasyLogScheduledExecutorInstance().scheduleWithFixedDelay(() -> {
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

    public static void pushLoggerConfig() {
        JedisPool jedisPool = Singleton.get(JedisPool.class);
        AppBasicInfo appBasicInfo = Singleton.get(AppBasicInfo.class);
        LoggingSystem loggingSystem = SpringUtil.getBean(LoggingSystem.class);
        List<LoggerConfiguration> loggerConfigurations = loggingSystem.getLoggerConfigurations();
        List<LoggerConfig> loggerConfigs = loggerConfigurations.stream().map(n -> LoggerConfig.builder()
                .loggerName(n.getName())
                .configuredLevel(Optional.ofNullable(n.getConfiguredLevel()).map(Enum::name).orElse("null"))
                .effectiveLevel(Optional.ofNullable(n.getEffectiveLevel()).map(Enum::name).orElse("null"))
                .build()).collect(Collectors.toList());
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(EasyLogConstants.LOGGER_CONFIG + appBasicInfo.getAppName(), appBasicInfo.getNamespace(), JSONUtil.toJsonStr(loggerConfigs));
        } catch (JedisConnectionException e) {
            log.error("delayPushLoggerConfig error [{}]", e.getMessage());
        }
    }
}
