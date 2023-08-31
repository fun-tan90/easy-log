package fun.tan90.easy.log.log4j.appender;


import cn.hutool.core.exceptions.ExceptionUtil;
import fun.tan90.easy.log.common.EasyLogManager;
import fun.tan90.easy.log.common.MqttManager;
import fun.tan90.easy.log.common.constant.EasyLogConstants;
import fun.tan90.easy.log.common.model.LogTransferred;
import fun.tan90.easy.log.common.utils.LocalhostUtil;
import com.yomahub.tlog.context.TLogContext;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.slf4j.helpers.MessageFormatter;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/12 18:16
 */
public class EasyLogAppender extends AppenderSkeleton {

    private static final AtomicBoolean INIT = new AtomicBoolean(false);

    private final static BlockingQueue<LogTransferred> BLOCKING_QUEUE = new ArrayBlockingQueue<>(EasyLogManager.GLOBAL_CONFIG.getQueueSize());

    @Override
    protected void append(LoggingEvent loggingEvent) {
        LogTransferred logTransferred = transferLog(loggingEvent);
        if (!BLOCKING_QUEUE.offer(logTransferred)) {
            System.err.println("Easy-Log BlockingQueue add failed");
        }
        if (INIT.compareAndSet(false, true)) {
            MqttManager.initMessageChannel();
            MqttManager.schedulePushLog(BLOCKING_QUEUE);
        }
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }


    /**
     * 日志格式转换
     *
     * @param logEvent
     * @return
     */
    private LogTransferred transferLog(LoggingEvent logEvent) {
        long timeStamp = logEvent.getTimeStamp();
        Level level = logEvent.getLevel();
        String loggerName = logEvent.getLoggerName();
        String threadName = logEvent.getThreadName();
        Map<String, String> mdc = logEvent.getProperties();

        LocationInfo locationInfo = logEvent.getLocationInformation();
        String method = locationInfo.getMethodName();
        String lineNumber = locationInfo.getLineNumber();

        String content = "-";
        if (level.equals(Level.ERROR)) {
            ThrowableInformation throwableInformation = logEvent.getThrowableInformation();
            if (throwableInformation != null) {
                String[] args = new String[]{logEvent.getRenderedMessage() + "\n" + ExceptionUtil.stacktraceToString(throwableInformation.getThrowable())};
                content = MessageFormatter.arrayFormat("{}", args).getMessage();
            } else {
                content = logEvent.getRenderedMessage();
            }
        } else {
            content = logEvent.getRenderedMessage();
        }
        return LogTransferred.builder()
                .timestamp(timeStamp)
                .seq(EasyLogManager.SEQ.incrementAndGet())
                .appName(EasyLogManager.GLOBAL_CONFIG.getAppName())
                .namespace(EasyLogManager.GLOBAL_CONFIG.getNamespace())
                .level(level.toString())
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
