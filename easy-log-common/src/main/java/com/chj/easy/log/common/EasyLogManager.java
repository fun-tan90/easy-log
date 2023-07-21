package com.chj.easy.log.common;


import com.chj.easy.log.common.threadpool.EasyLogThreadFactory;
import com.chj.easy.log.common.window.SlidingWindow;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 10:07
 */
public class EasyLogManager {
    public final static ExecutorService EASY_LOG_FIXED_THREAD_POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new EasyLogThreadFactory("el"));

    public final static ScheduledExecutorService EASY_LOG_SCHEDULED_EXECUTOR = Executors.newScheduledThreadPool(4, new EasyLogThreadFactory("el"));

    public static final SlidingWindow ERROR_SPEED_SLIDING_WINDOW = new SlidingWindow(1000, 5);

    public static final SlidingWindow WARN_SPEED_SLIDING_WINDOW = new SlidingWindow(1000, 5);

    public static final SlidingWindow INFO_SPEED_SLIDING_WINDOW = new SlidingWindow(1000, 5);

    public static final SlidingWindow DEBUG_SPEED_SLIDING_WINDOW = new SlidingWindow(1000, 5);

    public static final SlidingWindow TRACE_SPEED_SLIDING_WINDOW = new SlidingWindow(1000, 5);

    public static void logInputSpeed(String level, int count) {
        switch (level.toLowerCase()) {
            case "error":
                ERROR_SPEED_SLIDING_WINDOW.addCount(count);
                break;
            case "warn":
                WARN_SPEED_SLIDING_WINDOW.addCount(count);
                break;
            case "info":
                INFO_SPEED_SLIDING_WINDOW.addCount(count);
                break;
            case "debug":
                DEBUG_SPEED_SLIDING_WINDOW.addCount(count);
                break;
            case "trace":
                TRACE_SPEED_SLIDING_WINDOW.addCount(count);
                break;
            default:
        }
    }

    public static Map<String, Integer> statsLogInputSpeed() {
        Map<String, Integer> statsMap = new HashMap<>(5);
        statsMap.put("error", ERROR_SPEED_SLIDING_WINDOW.getWindowSum());
        statsMap.put("warn", WARN_SPEED_SLIDING_WINDOW.getWindowSum());
        statsMap.put("info", INFO_SPEED_SLIDING_WINDOW.getWindowSum());
        statsMap.put("debug", DEBUG_SPEED_SLIDING_WINDOW.getWindowSum());
        statsMap.put("trace", TRACE_SPEED_SLIDING_WINDOW.getWindowSum());
        return statsMap;
    }
}
