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
    String GROUP_ADMIN_NAME = "easy-log-admin-group";
    String GROUP_ADMIN_CONSUMER_NAME = "easy-log-admin-group-consumer";

    String GROUP_COLLECTOR_NAME = "easy-log-collector-group";
    String GROUP_COLLECTOR_CONSUMER_NAME = "easy-log-collector-group-consumer";

    String INDEX_MAPPING_PATH = "index_mapping/{}.json";

    String LOG_TOPIC_COLLECTOR = "easy-log-collector";

    String LOG_TOPIC_ADMIN = "easy-log-admin";

    String CAPTCHA_IMG = "CAPTCHA_IMG:";
}
