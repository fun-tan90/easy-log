package com.chj.easy.log.common.constant;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ClassLoaderUtil;

/**
 * 静态变量类
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
public interface EasyLogConstants {

    String EASY_LOG_VERSION = "1.1.2";

    String EASY_LOG_BUILD_DATE = "2023-07-31 09:42:00";

    String EASY_LOG_START_UP_TIME = DateUtil.now();

    Boolean T_LOG_CONTEXT_PRESENT = ClassLoaderUtil.isPresent("com.yomahub.tlog.context.TLogContext");

    String COLLECTOR_SCAN_BASE_PACKAGES = "com.chj.easy.log.collector";

    String COMPUTE_SCAN_BASE_PACKAGES = "com.chj.easy.log.compute";

    String ADMIN_SCAN_BASE_PACKAGES = "com.chj.easy.log.admin";

    String CORE_SCAN_BASE_PACKAGES = "com.chj.easy.log.core";

    String REDIS_STREAM_KEY = "easy-log";

    String GROUP_COMPUTE_NAME = "easy-log-compute-group";

    String GROUP_COLLECTOR_NAME = "easy-log-collector-group";

    String CONSUMER_COMPUTE_NAME = "easy-log-compute-group-consumer";

    String CONSUMER_COLLECTOR_NAME = "easy-log-collector-group-consumer";

    String ILM_PATH = "es/easy-log-ilm.json";

    String ILM_POLICY_NAME = "easy-log-policy";

    String INDEX_TEMPLATE_PATH = "es/easy-log-index-template.json";

    String INDEX_TEMPLATE_NAME = "easy-log-template";

    String EASY_LOG_TOPIC = "easy-log";

    String CAPTCHA_IMG = "CAPTCHA_IMG:";

    String REAL_TIME_FILTER_SUBSCRIBING_CLIENTS = "REAL_TIME_FILTER:SUBSCRIBING_CLIENTS";

    String REAL_TIME_FILTER_RULES = "REAL_TIME_FILTER:RULES:";

    String REAL_TIME_FILTER_Z_SET = "REAL_TIME_FILTER:ZSET:";

    String LOG_ALARM_RULES = "LOG_ALARM_RULES:";

    String LOG_ALARM_RULES_TOPIC = "log_alarm_rules/";

    String LOG_REAL_TIME_FILTER_RULES_TOPIC = "log_real_time_filter_rules/";

    String S_W_LOG_ALARM = "S_W:LOG_ALARM:";

    String LOG_ALARM = "LOG_ALARM";

    String LOG_ALARM_LOCK = "LOG_ALARM_LOCK:";

    String S_W_LOG_INPUT_SPEED = "S_W:LOG_INPUT_SPEED:";

    String LOG_ALARM_TOPIC = "easy-log/log_alarm";

    String LOG_AFTER_FILTERED_TOPIC = "easy-log/after-filtered/";

    String LOG_INPUT_SPEED_TOPIC = "easy-log/stats-log-input-speed";

    String LOG_INPUT_SPEED_LOCK = "LOG_INPUT_SPEED_LOCK";

    String SLIDING_WINDOW_LUA_PATH = "lua/slidingWindow.lua";

    String SLIDING_WINDOW_COUNT_LUA_PATH = "lua/slidingWindowCount.lua";

    String LOG_ALARM_PLATFORM = "LOG_ALARM_PLATFORM:";

    String MQTT_CLIENT_ID_SERVER_PREFIX = "easy_log_server:";

    String MQTT_CLIENT_ID_FRONT_PREFIX = "easy_log_front:";

    String MQTT_CLIENT_ID_APP_PREFIX = "easy_log_app:";

    String MQTT_CLIENT_USERNAME = "mica";

    String MQTT_CLIENT_PASSWORD = "mica";

    String MQTT_CMD_DOWN_PREFIX = "cmd/down";

    String MQTT_CMD_DOWN = MQTT_CMD_DOWN_PREFIX + "/{}/{}";

    String MQTT_CMD_UP_PREFIX = "cmd/up";

    String MQTT_CMD_UP = MQTT_CMD_UP_PREFIX + "/{}/{}";
}
