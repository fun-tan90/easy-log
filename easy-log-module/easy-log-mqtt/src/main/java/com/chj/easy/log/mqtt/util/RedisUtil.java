package com.chj.easy.log.mqtt.util;

import net.dreamlu.mica.core.utils.CharPool;


/**
 * description redis 工具
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/31 22:07
 */
public class RedisUtil {

    /**
     * 转换成 redis 的 pattern 规则
     *
     * @return pattern
     */
    public static String getTopicPattern(String topicFilter) {
        // mqtt 分享主题 $share/{ShareName}/{filter}
        return topicFilter
                .replace(CharPool.PLUS, CharPool.STAR)
                .replace(CharPool.HASH, CharPool.STAR);
    }

}
