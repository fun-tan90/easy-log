package com.chj.easy.log.common.constant;

/**
 * 静态变量类
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
public interface EasyLogConstants {
    String EASY_LOG_VERSION = "1.0.3";

    String COLLECTOR_SCAN_BASE_PACKAGES = "com.chj.easy.log.collector";

    String COMPUTE_SCAN_BASE_PACKAGES = "com.chj.easy.log.compute";

    String ADMIN_SCAN_BASE_PACKAGES = "com.chj.easy.log.admin";

    String CORE_SCAN_BASE_PACKAGES = "com.chj.easy.log.core";

    String INDEX_FIXED_PREFIX = "daily-easy-log-";

    String STREAM_KEY = "easy-log";

    String GROUP_COMPUTE_NAME = "easy-log-compute-group";

    String GROUP_COMPUTE_CONSUMER_NAME = "easy-log-compute-group-consumer";

    String GROUP_COLLECTOR_NAME = "easy-log-collector-group";

    String GROUP_COLLECTOR_CONSUMER_NAME = "easy-log-collector-group-consumer";

    String INDEX_MAPPING_PATH = "index_mapping/{}.json";

    String LOG_TOPIC_COLLECTOR = "easy-log-collector";

    String LOG_TOPIC_COMPUTE = "easy-log-compute";

    String LOG_TOPIC_ADMIN = "easy-log-admin";

    String CAPTCHA_IMG = "CAPTCHA_IMG:";

    String MQTT_ONLINE_CLIENTS = "MQTT_ONLINE_CLIENTS";

    String REAL_TIME_FILTER_RULES = "REAL_TIME_FILTER_RULES:";

    String AFTER_FILTER_TOPIC = "easy-log/after-filter";

    String INPUT_SPEED_TOPIC = "easy-log/stats-log-input-speed";

    String SLIDING_WINDOW_PATH = "lua/slidingWindow.lua";

    String SLIDING_WINDOW_COUNT_PATH = "lua/slidingWindowCount.lua";
}
