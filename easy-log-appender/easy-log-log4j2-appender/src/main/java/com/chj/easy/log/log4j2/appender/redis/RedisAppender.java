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
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.message.Message;
import org.slf4j.helpers.MessageFormatter;
import redis.clients.jedis.JedisPool;

import java.io.Serializable;
import java.util.Map;
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
@Getter
@Setter
@Plugin(name = RedisAppender.PLUGIN_NAME, category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class RedisAppender extends AbstractAppender {

    public static final String PLUGIN_NAME = "RedisAppender";

    private static BlockingQueue<LogTransferred> queue;

    private ScheduledFuture<?> scheduledFuture;

    private String appName;

    private String appEnv;

    private int maxPushSize;

    private int queueSize;

    /**
     * single、sentinel、cluster
     */
    private String redisMode;

    private String redisAddress;

    private String redisPass;

    private int redisDb;

    private int redisConnectionTimeout;

    private long redisStreamMaxLen;

    private int redisPoolMaxTotal;

    private int redisPoolMaxIdle;

    protected RedisAppender(
            String appName,
            String appEnv,
            int maxPushSize,
            int queueSize,
            String redisMode,
            String redisAddress,
            String redisPass,
            int redisDb,
            int redisConnectionTimeout,
            int redisStreamMaxLen,
            int redisPoolMaxTotal,
            int redisPoolMaxIdle,
            String name,
            Filter filter,
            Layout<? extends Serializable> layout,
            boolean ignoreExceptions,
            Property[] properties) {
        super(name, filter, layout, ignoreExceptions, properties);
        this.appName = appName;
        this.appEnv = appEnv;
        this.maxPushSize = maxPushSize;
        this.queueSize = queueSize;

        this.redisMode = redisMode;
        this.redisAddress = redisAddress;
        this.redisPass = redisPass;
        this.redisDb = redisDb;
        this.redisConnectionTimeout = redisConnectionTimeout;
        this.redisStreamMaxLen = redisStreamMaxLen;
        this.redisPoolMaxTotal = redisPoolMaxTotal;
        this.redisPoolMaxIdle = redisPoolMaxIdle;
    }

    @PluginFactory
    public static RedisAppender createAppender(
            @PluginAttribute(value = "appName", defaultString = "unknown") String appName,
            @PluginAttribute(value = "appEnv", defaultString = "default") String appEnv,
            @PluginAttribute(value = "maxPushSize", defaultInt = 500) int maxPushSize,
            @PluginAttribute(value = "queueSize", defaultInt = 10240) int queueSize,
            @PluginAttribute(value = "redisMode", defaultString = "single") String redisMode,
            @PluginAttribute(value = "redisAddress", defaultString = "127.0.0.1:6379") String redisAddress,
            @PluginAttribute(value = "redisPass") String redisPass,
            @PluginAttribute(value = "redisDb") int redisDb,
            @PluginAttribute(value = "redisConnectionTimeout", defaultInt = 1000) int redisConnectionTimeout,
            @PluginAttribute(value = "redisStreamMaxLen", defaultInt = 1000000) int redisStreamMaxLen,
            @PluginAttribute(value = "redisPoolMaxTotal", defaultInt = 30) int redisPoolMaxTotal,
            @PluginAttribute(value = "redisPoolMaxIdle", defaultInt = 30) int redisPoolMaxIdle,
            @PluginAttribute("name") String name,
            @PluginElement("Filter") final Filter filter,
            @PluginElement("Layout") Layout<? extends Serializable> layout) {
        queue = new ArrayBlockingQueue<>(queueSize);
        JedisPool jedisPool = RedisFactory.getJedisPool(redisMode, redisAddress, redisPass, redisDb, redisPoolMaxIdle, redisPoolMaxTotal, redisConnectionTimeout);
        // 仅启动单线推送消息，避免多线程下日志乱序问题
        EasyLogManager.EASY_LOG_SCHEDULED_EXECUTOR.scheduleWithFixedDelay(() -> {
            RedisFactory.push(queue, jedisPool, maxPushSize, redisStreamMaxLen);
        }, 5, 100, TimeUnit.MILLISECONDS);
        return new RedisAppender(
                appName,
                appEnv,
                maxPushSize,
                queueSize,
                redisMode,
                redisAddress,
                redisPass,
                redisDb,
                redisConnectionTimeout,
                redisStreamMaxLen,
                redisPoolMaxTotal,
                redisPoolMaxIdle,
                name,
                filter,
                layout,
                true,
                null
        );
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
