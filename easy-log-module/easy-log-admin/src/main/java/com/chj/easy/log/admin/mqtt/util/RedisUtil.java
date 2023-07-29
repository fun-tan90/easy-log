package com.chj.easy.log.admin.mqtt.util;

import net.dreamlu.mica.core.utils.CharPool;

/**
 * redis 工具
 *
 * @author L.cm
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
