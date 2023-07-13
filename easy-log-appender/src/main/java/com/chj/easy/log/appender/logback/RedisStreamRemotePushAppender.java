package com.chj.easy.log.appender.logback;


import com.alibaba.fastjson.JSON;
import com.chj.easy.log.appender.LogTransferred;
import com.chj.easy.log.appender.LogTransferredContext;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/12 18:18
 */
public class RedisStreamRemotePushAppender extends AbstractRemotePushAppender {

    @Override
    void push(BlockingQueue<LogTransferred> queue) {
        List<LogTransferred> logTransferredList = LogTransferredContext.getLogTransferred();
        while (queue.remainingCapacity() != -1) {
            LogTransferred logTransferred = queue.poll();
            if (logTransferred == null) {
                break;
            }
            logTransferredList.add(logTransferred);
            if (logTransferredList.size() == batchPushSize) {
                break;
            }
        }
        if (!logTransferredList.isEmpty()) {
            System.out.println(Thread.currentThread().getName() + " pool " + JSON.toJSONString(logTransferredList));
        }
    }
}