package com.chj.easy.log.common.constant;

/**
 * 静态变量类
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
public interface EasyLogConstants {
    String EASY_LOG_VERSION = "1.0.1";

    String COLLECTOR_SCAN_BASE_PACKAGES = "com.chj.easy.log.collector";

    String ADMIN_SCAN_BASE_PACKAGES = "com.chj.easy.log.admin";

    String CORE_SCAN_BASE_PACKAGES = "com.chj.easy.log.core";

    String INDEX_FIXED_PREFIX = "daily-easy-log-";

    String STREAM_KEY = "easy-log";

    String GROUP_NAME = "easy-log-group";

    String GROUP_CONSUMER_NAME = "easy-log-group-consumer";

    String INDEX_MAPPING_PATH = "index_mapping/{}.json";

    String LOG_TOPIC = "easy-log";
}
