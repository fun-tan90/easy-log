package com.chj.easy.log.appender.logback;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.util.Duration;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.chj.easy.log.appender.LogTransferred;
import com.chj.easy.log.appender.LogTransferredContext;
import com.yomahub.tlog.context.TLogContext;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.helpers.MessageFormatter;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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
public abstract class AbstractRemotePushAppender extends AppenderBase<ILoggingEvent> {

    private static final Duration DEFAULT_EVENT_DELAY_TIMEOUT = new Duration(100);

    public static final int DEFAULT_QUEUE_SIZE = 512;

    public static final int DEFAULT_PUSH_BATCH_SIZE = 24;

    private BlockingQueue<LogTransferred> queue;

    private ScheduledFuture<?> scheduledFuture;

    private int queueSize = DEFAULT_QUEUE_SIZE;

    private Duration eventDelayLimit = DEFAULT_EVENT_DELAY_TIMEOUT;

    int batchPushSize = DEFAULT_PUSH_BATCH_SIZE;

    @Override
    public void start() {
        if (isStarted()) {
            return;
        }
        initRemotePushClient();
        this.queue = new LinkedBlockingQueue<>(queueSize);
        this.scheduledFuture = getContext().getScheduledExecutorService().scheduleWithFixedDelay(() -> {
            try {
                LogTransferredContext.setLogTransferred(batchPushSize);
                push(queue);
            } finally {
                LogTransferredContext.clear();
            }
        }, 1, 100, TimeUnit.MILLISECONDS);
        super.start();
    }

    abstract void initRemotePushClient();

    abstract void push(BlockingQueue<LogTransferred> queue);

    @Override
    public void stop() {
        if (!isStarted()) {
            return;
        }
        this.scheduledFuture.cancel(true);
        super.stop();
    }

    @Override
    protected void append(ILoggingEvent logEvent) {
        if (logEvent == null || !isStarted()) {
            return;
        }
        LogTransferred logTransferred = transferLog(logEvent);
        try {
            boolean offered = this.queue.offer(logTransferred, eventDelayLimit.getMilliseconds(), TimeUnit.MILLISECONDS);
            if (!offered) {
                addWarn("Dropping event due to timeout limit of [" + eventDelayLimit + "] being exceeded");
            }
        } catch (InterruptedException e) {
            addError("Interrupted while appending event to AbstractRemotePushAppender", e);
        }
    }

    /**
     * 日志格式转换
     *
     * @param logEvent
     * @return
     */
    private LogTransferred transferLog(ILoggingEvent logEvent) {
        long timeStamp = logEvent.getTimeStamp();
        Level level = logEvent.getLevel();
        String loggerName = logEvent.getLoggerName();
        String threadName = logEvent.getThreadName();
        Map<String, String> mdc = logEvent.getMDCPropertyMap();
        String method = "-";
        String lineNumber = "-";
        if (logEvent.hasCallerData()) {
            StackTraceElement[] callerData = logEvent.getCallerData();
            StackTraceElement stackTraceElement = callerData[0];
            method = stackTraceElement.getMethodName();
            lineNumber = String.valueOf(stackTraceElement.getLineNumber());
        }
        String content = "-";
        if (level.equals(Level.ERROR)) {
            if (logEvent.getThrowableProxy() != null) {
                ThrowableProxy throwableProxy = (ThrowableProxy) logEvent.getThrowableProxy();
                String[] args = new String[]{logEvent.getFormattedMessage() + "\n" + ExceptionUtil.stacktraceToString(throwableProxy.getThrowable())};
                content = MessageFormatter.arrayFormat("{}", args).getMessage();
            } else {
                Object[] argumentArray = logEvent.getArgumentArray();
                if (argumentArray != null) {
                    for (int i = 0; i < argumentArray.length; i++) {
                        if (argumentArray[i] instanceof Throwable) {
                            argumentArray[i] = ExceptionUtil.stacktraceToOneLineString((Throwable) argumentArray[i]);
                        }
                    }
                    StringBuilder message = new StringBuilder(logEvent.getMessage());
                    for (Object argument : argumentArray) {
                        message.append("\n").append(argument);
                    }
                    content = message.toString();
                }
            }
        } else {
            content = logEvent.getFormattedMessage();
        }
        return LogTransferred.builder()
                .timeStamp(timeStamp)
                .level(level.levelStr)
                .loggerName(loggerName)
                .threadName(threadName)
                .traceId(TLogContext.getTraceId())
                .spanId(TLogContext.getSpanId())
                .currIp(TLogContext.getCurrIp())
                .preIp(TLogContext.getPreIp())
                .mdc(mdc)
                .method(method)
                .lineNumber(lineNumber)
                .content(content)
                .build();
    }
}
