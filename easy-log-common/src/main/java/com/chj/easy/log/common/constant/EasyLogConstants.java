package com.chj.easy.log.common.constant;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ClassLoaderUtil;


/**
 * description 静态变量类
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/4 18:01
 */
public interface EasyLogConstants {

    String EASY_LOG_START_UP_TIME = DateUtil.now();

    Boolean T_LOG_CONTEXT_PRESENT = ClassLoaderUtil.isPresent("com.yomahub.tlog.context.TLogContext");

    String COLLECTOR_SCAN_BASE_PACKAGES = "com.chj.easy.log.collector";

    String COMPUTE_SCAN_BASE_PACKAGES = "com.chj.easy.log.compute";

    String ADMIN_SCAN_BASE_PACKAGES = "com.chj.easy.log.admin";

    String CORE_SCAN_BASE_PACKAGES = "com.chj.easy.log.core";

    String ILM_PATH = "es/easy-log-ilm.json";

    String ILM_POLICY_NAME = "easy-log-policy";

    String INDEX_TEMPLATE_PATH = "es/easy-log-index-template.json";

    String INDEX_TEMPLATE_NAME = "easy-log-template";

    String EASY_LOG_TOPIC = "easy-log";

    String CAPTCHA_IMG = "CAPTCHA_IMG:";

    int MQTT_MD5_SUB_INDEX = 12;

    String REAL_TIME_FILTER_SUBSCRIBING_CLIENTS = "REAL_TIME_FILTER:SUBSCRIBING_CLIENTS";

    String REAL_TIME_FILTER_RULES = "REAL_TIME_FILTER:RULES:";

    String REAL_TIME_FILTER_Z_SET = "REAL_TIME_FILTER:ZSET:";

    String LOG_ALARM_RULES = "LOG_ALARM_RULES:";

    String S_W_LOG_ALARM = "S_W:LOG_ALARM:";

    String LOG_ALARM = "LOG_ALARM";

    String LOG_ALARM_LOCK = "LOG_ALARM_LOCK:";

    String S_W_LOG_INPUT_SPEED = "S_W:LOG_INPUT_SPEED:";

    String LOG_INPUT_SPEED_LOCK = "LOG_INPUT_SPEED_LOCK";

    String SLIDING_WINDOW_LUA_PATH = "lua/slidingWindow.lua";

    String SLIDING_WINDOW_COUNT_LUA_PATH = "lua/slidingWindowCount.lua";

    String LOG_ALARM_PLATFORM = "LOG_ALARM_PLATFORM:";

    /**
     * mqtt客户端
     */
    String MQTT_CLIENT_ID_FRONT_PREFIX = "easy_log_front:";

    String MQTT_CLIENT_ID_CLIENT_PREFIX = "easy_log_client:";

    /**
     * 主题相关
     */
    String LOG_REAL_TIME_FILTER_RULES_TOPIC = "el/log_real_time_filter_rules/";

    String MQTT_CMD_DOWN_PREFIX = "el/cmd/down";

    String MQTT_CMD_DOWN_TOPIC = MQTT_CMD_DOWN_PREFIX + "/{}/{}";

    String MQTT_CMD_UP_PREFIX = "el/cmd/up";

    String MQTT_CMD_UP_TOPIC = MQTT_CMD_UP_PREFIX + "/{}/{}";

    String MQTT_LOG_PREFIX = "el/log";

    String MQTT_LOG_TOPIC = MQTT_LOG_PREFIX + "/{}/{}";

    String MQTT_LOG_ALARM_TOPIC = "el/log_alarm";

    String MQTT_LOG_AFTER_FILTERED_TOPIC = "el/after-filtered/";

    String MQTT_LOG_INPUT_SPEED_TOPIC = "el/stats-log-input-speed";

    String MQTT_LOG_ALARM_RULES_TOPIC = "el/log_alarm_rules/";
}
