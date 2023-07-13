package com.chj.easy.log.common;


import com.chj.easy.log.common.threadpool.EasyLogThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 10:07
 */
public class EasyLogManager {
    public final static ExecutorService EASY_LOG_FIXED_THREAD_POOL = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new EasyLogThreadFactory("easy-log"));
}
