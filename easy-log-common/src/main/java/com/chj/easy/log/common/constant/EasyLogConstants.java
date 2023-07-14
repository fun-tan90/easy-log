package com.chj.easy.log.common.constant;

import cn.hutool.core.io.resource.ResourceUtil;

/**
 * 静态变量类
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
public interface EasyLogConstants {
    String SCAN_BASE_PACKAGES = "com.chj.easy.log.server";

    String INDEX_FIXED_PREFIX = "daily-easy-log-";

    String STREAM_KEY = "easy-log";

    String GROUP_NAME = "easy-log-group";

    String GROUP_CONSUMER_NAME = "easy-log-group-consumer";

    String EASY_LOG_INDEX_MAPPINGS = ResourceUtil.readUtf8Str("easyLogMappings.json");
}
