package com.chj.easy.log.appender.logback;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.util.Duration;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.chj.easy.log.common.model.LogTransferred;
import com.yomahub.tlog.context.TLogContext;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.helpers.MessageFormatter;

import java.util.Date;
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

    private int queueSize = 1024;

    private Duration eventDelayLimit = new Duration(100);

    @Override
    public void start() {
        if (!enable || isStarted()) {
            return;
        }
        initRemotePushClient();
        this.queue = new LinkedBlockingQueue<>(queueSize);
        this.scheduledFuture = getContext().getScheduledExecutorService().scheduleWithFixedDelay(() -> {
            push(queue);
        }, 1, 50, TimeUnit.MILLISECONDS);
        super.start();
    }

    protected abstract void initRemotePushClient();

    protected abstract void push(BlockingQueue<LogTransferred> queue);

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
                .build();
    }
}
