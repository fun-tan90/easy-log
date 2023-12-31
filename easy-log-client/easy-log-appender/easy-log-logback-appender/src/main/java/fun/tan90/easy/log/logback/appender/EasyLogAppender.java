package fun.tan90.easy.log.logback.appender;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import cn.hutool.core.exceptions.ExceptionUtil;
import fun.tan90.easy.log.common.EasyLogManager;
import fun.tan90.easy.log.common.MqttManager;
import fun.tan90.easy.log.common.constant.EasyLogConstants;
import fun.tan90.easy.log.common.model.LogTransferred;
import fun.tan90.easy.log.common.utils.LocalhostUtil;
import com.yomahub.tlog.context.TLogContext;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.helpers.MessageFormatter;

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
@Getter
@Setter
public class EasyLogAppender extends AppenderBase<ILoggingEvent> {

    private BlockingQueue<LogTransferred> blockingQueue;

    @Override
    public void start() {
        if (isStarted()) {
            return;
        }
        this.blockingQueue = new ArrayBlockingQueue<>(EasyLogManager.GLOBAL_CONFIG.getQueueSize());
        MqttManager.initMessageChannel();
        MqttManager.schedulePushLog(blockingQueue);
        super.start();
    }

    @Override
    protected void append(ILoggingEvent logEvent) {
        if (logEvent == null || !isStarted()) {
            return;
        }
        LogTransferred logTransferred = transferLog(logEvent);
        if (!this.blockingQueue.offer(logTransferred)) {
            addError("Easy-Log BlockingQueue add failed");
        }
    }

    /**
     * 日志格式转换
     *
     * @param logEvent logback日志读写
     * @return 转换后的LogTransferred
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
            Object[] argumentArray = logEvent.getArgumentArray();
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
                .timestamp(timeStamp)
                .seq(EasyLogManager.SEQ.incrementAndGet())
                .appName(EasyLogManager.GLOBAL_CONFIG.getAppName())
                .namespace(EasyLogManager.GLOBAL_CONFIG.getNamespace())
                .level(level.levelStr)
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
