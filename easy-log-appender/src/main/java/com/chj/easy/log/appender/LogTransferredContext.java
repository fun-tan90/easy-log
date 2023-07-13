package com.chj.easy.log.appender;

import java.util.ArrayList;
import java.util.List;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/12 21:42
 */
public class LogTransferredContext {

    private final static ThreadLocal<List<LogTransferred>> LOG_TRANSFERRED_TL = new ThreadLocal<>();

    public static void setLogTransferred(int batchPushSize) {
        LOG_TRANSFERRED_TL.set(new ArrayList<>(batchPushSize));
    }

    public static List<LogTransferred> getLogTransferred() {
        return LOG_TRANSFERRED_TL.get();
    }

    public static void clear() {
        LOG_TRANSFERRED_TL.remove();
    }
}
