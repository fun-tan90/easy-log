package fun.tan90.easy.log.common.threadpool;


import cn.hutool.core.lang.Singleton;

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
public class EasyLogThreadPool {

    public static ExecutorService newEasyLogFixedPoolInstance() {
        return Singleton.get("EASY_LOG_FIXED_THREAD_POOL", () -> Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new EasyLogThreadFactory("el")));
    }

    public static ScheduledExecutorService newEasyLogScheduledExecutorInstance() {
        return Singleton.get("EASY_LOG_SCHEDULED_EXECUTOR", () -> Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(), new EasyLogThreadFactory("el")));
    }
}
