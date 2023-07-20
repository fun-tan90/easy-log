package com.chj.easy.log.log4j2.appender.redis;


import cn.hutool.core.exceptions.ExceptionUtil;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.model.LogTransferred;
import com.chj.easy.log.core.appender.RedisFactory;
import com.yomahub.tlog.context.TLogContext;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.mom.kafka.KafkaAppender;
import org.apache.logging.log4j.core.appender.mom.kafka.KafkaManager;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.message.Message;
import org.slf4j.helpers.MessageFormatter;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/12 18:16
 */
@Plugin(name = RedisAppender.PLUGIN_NAME, category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public final class RedisAppender extends AbstractAppender {

    public static final String PLUGIN_NAME = "RedisAppender";

    private final String appName;

    private final String appEnv;

    private final BlockingQueue<LogTransferred> queue;

    private final JedisPool jedisPool;

    private ScheduledFuture<?> scheduledFuture;

    private RedisAppender(final String name,
                          final Layout<? extends Serializable> layout,
                          final Filter filter,
                          final boolean ignoreExceptions,
                          final Property[] properties,
                          final String appName,
                          final String appEnv,
                          final JedisPool jedisPool,
                          final BlockingQueue<LogTransferred> queue) {
        super(name, filter, layout, ignoreExceptions, properties);
        this.appName = appName;
        this.appEnv = appEnv;
        this.jedisPool = jedisPool;
        this.queue = queue;
    }

    @Setter
    @Getter
    public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B>
            implements org.apache.logging.log4j.core.util.Builder<RedisAppender> {

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
        public RedisAppender build() {
            final Layout<? extends Serializable> layout = getLayout();

            JedisPool jedisPool = RedisFactory.getJedisPool(redisMode, redisAddress, redisPass, redisDb, redisPoolMaxIdle, redisPoolMaxTotal, redisConnectionTimeout);

            BlockingQueue<LogTransferred> queue = new ArrayBlockingQueue<>(queueSize);
            return new RedisAppender(
                    getName(),
                    layout,
                    getFilter(),
                    isIgnoreExceptions(),
                    getPropertyArray(),
                    getAppName(),
                    getAppEnv(),
                    jedisPool,
                    queue);
        }
    }

    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }

    @Override
    public void start() {
        super.start();
        // 仅启动单线推送消息，避免多线程下日志乱序问题
        this.scheduledFuture = EasyLogManager.EASY_LOG_SCHEDULED_EXECUTOR.scheduleWithFixedDelay(() -> {
            RedisFactory.push(queue, jedisPool, 100, 100000);
        }, 5, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void append(LogEvent logEvent) {
        if (logEvent == null || !isStarted()) {
            return;
        }
        LogTransferred logTransferred = transferLog(logEvent);
        if (!queue.offer(logTransferred)) {
            System.err.println("Easy-Log BlockingQueue add failed");
        }
    }

    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        setStopping();
        boolean stopped = super.stop(timeout, timeUnit, false);
        RedisFactory.closeJedisPool();
        setStopped();
        return stopped;
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
