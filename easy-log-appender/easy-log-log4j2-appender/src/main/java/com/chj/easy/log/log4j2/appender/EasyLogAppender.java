package com.chj.easy.log.log4j2.appender;


import cn.hutool.core.exceptions.ExceptionUtil;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.common.model.LogTransferred;
import com.chj.easy.log.common.utils.LocalhostUtil;
import com.chj.easy.log.core.appender.MqttManager;
import com.yomahub.tlog.context.TLogContext;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.message.Message;
import org.slf4j.helpers.MessageFormatter;

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
@Plugin(name = EasyLogAppender.PLUGIN_NAME, category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public final class EasyLogAppender extends AbstractAppender {

    public static final String PLUGIN_NAME = "EasyLog";

    private final BlockingQueue<LogTransferred> blockingQueue;

    private EasyLogAppender(final String name,
                            final Layout<? extends Serializable> layout,
                            final Filter filter,
                            final boolean ignoreExceptions,
                            final Property[] properties,
                            final BlockingQueue<LogTransferred> blockingQueue
    ) {
        super(name, filter, layout, ignoreExceptions, properties);
        this.blockingQueue = blockingQueue;
    }

    @Getter
    @Setter
    public static class Builder<B extends Builder<B>> extends AbstractAppender.Builder<B>
            implements org.apache.logging.log4j.core.util.Builder<EasyLogAppender> {

        @Override
        public EasyLogAppender build() {
            BlockingQueue<LogTransferred> blockingQueue = new ArrayBlockingQueue<>(EasyLogManager.GLOBAL_CONFIG.getQueueSize());
            return new EasyLogAppender(
                    getName(),
                    getLayout(),
                    getFilter(),
                    isIgnoreExceptions(),
                    getPropertyArray(),
                    blockingQueue
            );
        }
    }

    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }

    @Override
    public void start() {
        MqttManager.initMessageChannel();
        MqttManager.schedulePushLog(blockingQueue);
        super.start();
    }

    @Override
    public void append(final LogEvent logEvent) {
        if (logEvent == null || !isStarted()) {
            return;
        }
        LogTransferred logTransferred = transferLog(logEvent);
        if (!blockingQueue.offer(logTransferred)) {
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
                .timestamp(timeStamp)
                .seq(EasyLogManager.SEQ.incrementAndGet())
                .appName(EasyLogManager.GLOBAL_CONFIG.getAppName())
                .namespace(EasyLogManager.GLOBAL_CONFIG.getNamespace())
                .level(level.name())
                .loggerName(loggerName)
                .threadName(threadName)
                .traceId(EasyLogConstants.T_LOG_CONTEXT_PRESENT ? TLogContext.getTraceId() : "-")
                .spanId(EasyLogConstants.T_LOG_CONTEXT_PRESENT ? TLogContext.getSpanId() : "-")
                .currIp(LocalhostUtil.getHostIp())
                .preIp(EasyLogConstants.T_LOG_CONTEXT_PRESENT ? TLogContext.getPreIp() : "-")
                .method(method)
                .lineNumber(lineNumber)
                .content(content)
                .mdc(mdc)
                .build();
    }
}
