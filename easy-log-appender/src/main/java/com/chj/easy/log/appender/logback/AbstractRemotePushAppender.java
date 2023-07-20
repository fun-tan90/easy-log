package com.chj.easy.log.appender.logback;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.util.Duration;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.chj.easy.log.common.EasyLogManager;
import com.chj.easy.log.common.model.LogTransferred;
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

    private BlockingQueue<LogTransferred> queue;

    private ScheduledFuture<?> scheduledFuture;

    private boolean enable = true;

    private String appName = "unknown";

    private String appEnv = "default";

    private int maxPushSize = 100;

    private int queueSize = 10240;

    private Duration eventDelayLimit = new Duration(100);

    @Override
    public void start() {
        if (!enable || isStarted()) {
            return;
        }
        initRemotePushClient();
        this.queue = new LinkedBlockingQueue<>(queueSize);
        this.scheduledFuture = EasyLogManager.EASY_LOG_SCHEDULED_EXECUTOR.scheduleWithFixedDelay(() -> {
            push(queue, maxPushSize);
        }, 5, 50, TimeUnit.MILLISECONDS);
        super.start();
    }

    protected abstract void initRemotePushClient();

    protected abstract void closeRemotePushClient();

    protected abstract void push(BlockingQueue<LogTransferred> queue, int maxPushSize);

    @Override
    public void stop() {
        if (!isStarted()) {
            return;
        }
        if (!scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(true);
        }

        closeRemotePushClient();

        super.stop();
    }

    @Override
    protected void append(ILoggingEvent logEvent) {
        if (logEvent == null || !isStarted()) {
            return;
        }
        LogTransferred logTransferred = transferLog(logEvent);
        if (!this.queue.offer(logTransferred)) {
            addError("Easy-Log BlockingQueue add failed");
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
        Object[] argumentArray = logEvent.getArgumentArray();
        String content = "-";
        if (level.equals(Level.ERROR)) {
            if (logEvent.getThrowableProxy() != null) {
                ThrowableProxy throwableProxy = (ThrowableProxy) logEvent.getThrowableProxy();
                String[] args = new String[]{logEvent.getFormattedMessage() + "\n" + ExceptionUtil.stacktraceToString(throwableProxy.getThrowable())};
                content = MessageFormatter.arrayFormat("{}", args).getMessage();
            } else {
                if (argumentArray != null) {
                    for (int i = 0; i < argumentArray.length; i++) {
                        if (argumentArray[i] instanceof Throwable) {
                            argumentArray[i] = ExceptionUtil.stacktraceToOneLineString((Throwable) argumentArray[i]);
                        }
                    }
                    String message = logEvent.getMessage();
                    if (message != null && message.contains("{}")) {
                        content = MessageFormatter.arrayFormat(message, argumentArray).getMessage();
                    }
                } else {
                    content = logEvent.getFormattedMessage();
                }
            }
        } else {
            content = logEvent.getFormattedMessage();
        }

        return LogTransferred.builder()
                .timeStamp(timeStamp)
                .appName(appName)
                .appEnv(appEnv)
                .level(level.levelStr)
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
