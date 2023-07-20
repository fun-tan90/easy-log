package com.chj.easy.log.log4j2.appender.redis;


import cn.hutool.core.exceptions.ExceptionUtil;
import com.chj.easy.log.common.model.LogTransferred;
import com.chj.easy.log.core.appender.RedisFactory;
import com.yomahub.tlog.context.TLogContext;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.message.Message;
import org.slf4j.helpers.MessageFormatter;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/12 18:16
 */
@Plugin(name = EasyLogRedisAppender.PLUGIN_NAME, category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public final class EasyLogRedisAppender extends AbstractAppender {

    public static final String PLUGIN_NAME = "EasyLogRedis";

    private final String appName;

    private final String appEnv;

    private final BlockingQueue<LogTransferred> queue;

    private final JedisPool jedisPool;
    private final int maxPushSize;
    private final int redisStreamMaxLen;

    private EasyLogRedisAppender(final String name,
                                 final Layout<? extends Serializable> layout,
                                 final Filter filter,
                                 final boolean ignoreExceptions,
                                 final Property[] properties,
                                 final String appName,
                                 final String appEnv,
                                 final JedisPool jedisPool,
                                 final BlockingQueue<LogTransferred> queue,
                                 final int maxPushSize,
                                 final int redisStreamMaxLen
    ) {
        super(name, filter, layout, ignoreExceptions, properties);
        this.appName = appName;
        this.appEnv = appEnv;
        this.jedisPool = jedisPool;
        this.queue = queue;
        this.maxPushSize = maxPushSize;
        this.redisStreamMaxLen = redisStreamMaxLen;
    }

    @Getter
    @Setter
    public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B>
            implements org.apache.logging.log4j.core.util.Builder<EasyLogRedisAppender> {

        @PluginAttribute(value = "appName", defaultString = "unknown")
        private String appName;

        @PluginAttribute(value = "appEnv", defaultString = "default")
        private String appEnv;

        @PluginAttribute(value = "maxPushSize", defaultInt = 500)
        private int maxPushSize;

        @PluginAttribute(value = "queueSize", defaultInt = 10240)
        private int queueSize;

        @PluginAttribute(value = "redisMode", defaultString = "single")
        private String redisMode;

        @PluginAttribute(value = "redisAddress", defaultString = "127.0.0.1:6379")
        private String redisAddress;

        @PluginAttribute(value = "redisPass")
        private String redisPass;

        @PluginAttribute(value = "redisDb")
        private int redisDb;

        @PluginAttribute(value = "redisConnectionTimeout", defaultInt = 1000)
        private int redisConnectionTimeout;

        @PluginAttribute(value = "redisStreamMaxLen", defaultInt = 1000000)
        private int redisStreamMaxLen;

        @PluginAttribute(value = "redisPoolMaxTotal", defaultInt = 30)
        private int redisPoolMaxTotal;

        @PluginAttribute(value = "redisPoolMaxIdle", defaultInt = 30)
        private int redisPoolMaxIdle;

        @Override
        public EasyLogRedisAppender build() {
            JedisPool jedisPool = RedisFactory.getJedisPool(redisMode, redisAddress, redisPass, redisDb, redisPoolMaxIdle, redisPoolMaxTotal, redisConnectionTimeout);
            BlockingQueue<LogTransferred> queue = new ArrayBlockingQueue<>(queueSize);
            return new EasyLogRedisAppender(
                    getName(),
                    getLayout(),
                    getFilter(),
                    isIgnoreExceptions(),
                    getPropertyArray(),
                    getAppName(),
                    getAppEnv(),
                    jedisPool,
                    queue,
                    getMaxPushSize(),
                    getRedisStreamMaxLen()
            );
        }
    }

    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }

    @Override
    public void start() {
        if (!RedisFactory.ping()) {
            throw new JedisException("Could not get a resource from the jedis pool");
        }
        RedisFactory.schedulePush(queue, jedisPool, maxPushSize, redisStreamMaxLen);
        super.start();
    }

    @Override
    public void append(final LogEvent logEvent) {
        if (logEvent == null || !isStarted()) {
            return;
        }
        LogTransferred logTransferred = transferLog(logEvent);
        if (!queue.offer(logTransferred)) {
            System.err.println("Easy-Log BlockingQueue add failed");
        }
    }

    /**
     * 日志格式转换
     *
     * @param logEvent
     * @return
     */
    private LogTransferred transferLog(LogEvent logEvent) {
        long timeStamp = logEvent.getTimeMillis();
        Level level = logEvent.getLevel();
        String loggerName = logEvent.getLoggerName();
        String threadName = logEvent.getThreadName();
        Map<String, String> mdc = logEvent.getContextData().toMap();

        StackTraceElement stackTraceElement = logEvent.getSource();
        String method = stackTraceElement.getMethodName();
        String lineNumber = String.valueOf(stackTraceElement.getLineNumber());

        Message logMessage = logEvent.getMessage();
        Object[] argumentArray = logMessage.getParameters();
        String content = "-";
        if (level.equals(Level.ERROR)) {
            ThrowableProxy thrownProxy = logEvent.getThrownProxy();
            if (thrownProxy != null) {
                String[] args = new String[]{logMessage.getFormattedMessage() + "\n" + ExceptionUtil.stacktraceToString(thrownProxy.getThrowable())};
                content = MessageFormatter.arrayFormat("{}", args).getMessage();
            } else {
                if (argumentArray != null) {
                    for (int i = 0; i < argumentArray.length; i++) {
                        if (argumentArray[i] instanceof Throwable) {
                            argumentArray[i] = ExceptionUtil.stacktraceToOneLineString((Throwable) argumentArray[i]);
                        }
                    }
                    String format = logMessage.getFormat();
                    if (format != null && format.contains("{}")) {
                        content = MessageFormatter.arrayFormat(format, argumentArray).getMessage();
                    }
                } else {
                    content = logMessage.getFormattedMessage();
                }
            }
        } else {
            content = logMessage.getFormattedMessage();
        }

        return LogTransferred.builder()
                .timeStamp(timeStamp)
                .appName(appName)
                .appEnv(appEnv)
                .level(level.name())
                .loggerName(loggerName)
                .threadName(threadName)
                .traceId(TLogContext.getTraceId())
                .spanId(TLogContext.getSpanId())
                .currIp(TLogContext.getCurrIp())
                .preIp(TLogContext.getPreIp())
                .method(method)
                .lineNumber(lineNumber)
                .content(content)
                .mdc(mdc)
                .build();
    }
}
