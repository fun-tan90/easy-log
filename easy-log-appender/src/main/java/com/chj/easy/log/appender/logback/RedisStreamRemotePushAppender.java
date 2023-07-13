package com.chj.easy.log.appender.logback;


import com.alibaba.fastjson.JSON;
import com.chj.easy.log.appender.LogTransferred;
import com.chj.easy.log.appender.LogTransferredContext;
import com.chj.easy.log.appender.RemotePushClientFactory;
import com.chj.easy.log.common.constant.EasyLogConstants;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntryID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/12 18:18
 */
public class RedisStreamRemotePushAppender extends AbstractRemotePushAppender {
    private Jedis jedis = RemotePushClientFactory.getJedis("172.16.8.31", 6379, "root@123456!", 10);

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
            for (LogTransferred logTransferred : logTransferredList) {
                Map<String, String> map = new HashMap<>();
                map.put("content", logTransferred.getContent());
                StreamEntryID xadd = jedis.xadd(EasyLogConstants.STREAM_KEY, StreamEntryID.NEW_ENTRY, map, 100000, false);
                System.out.println(xadd.toString());
            }
        }
    }
}