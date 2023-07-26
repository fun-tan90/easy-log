package com.chj.easy.log.admin.service.impl;

import com.chj.easy.log.admin.model.cmd.LogRealTimeFilterCmd;
import com.chj.easy.log.admin.service.LogRealTimeFilterService;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.event.LogAlarmRegisterEvent;
import com.chj.easy.log.core.model.Topic;
import com.chj.easy.log.core.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/18 16:21
 */
@Slf4j(topic = EasyLogConstants.LOG_TOPIC_ADMIN)
@Service
public class LogRealTimeFilterServiceImpl implements LogRealTimeFilterService {

    @Resource
    ApplicationContext applicationContext;

    @Resource
    EsService esService;

    @Override
    public Topic subscribe(LogRealTimeFilterCmd logRealTimeFilterCmd) {
        long timestamp = System.currentTimeMillis();
        Map<String, String> realTimeFilterRules = new HashMap<>();
        realTimeFilterRules.put("timeStamp#gle", String.valueOf(timestamp));
        String namespace = logRealTimeFilterCmd.getNamespace();
        realTimeFilterRules.put("namespace#eq", namespace);

        List<String> appNameList = logRealTimeFilterCmd.getAppNameList();
        if (!CollectionUtils.isEmpty(appNameList)) {
            realTimeFilterRules.put("appName#should", String.join("%", appNameList));
        }
        List<String> levelList = logRealTimeFilterCmd.getLevelList();
        if (!CollectionUtils.isEmpty(levelList)) {
            realTimeFilterRules.put("level#should", String.join("%", levelList));
        }
        String loggerName = logRealTimeFilterCmd.getLoggerName();
        if (StringUtils.hasLength(loggerName)) {
            realTimeFilterRules.put("loggerName#eq", loggerName);
        }
        String lineNumber = logRealTimeFilterCmd.getLineNumber();
        if (StringUtils.hasLength(lineNumber)) {
            realTimeFilterRules.put("lineNumber#eq", lineNumber);
        }
        List<String> ipList = logRealTimeFilterCmd.getIpList();
        if (!CollectionUtils.isEmpty(ipList)) {
            realTimeFilterRules.put("currIp#should", String.join("%", ipList));
        }
        String content = logRealTimeFilterCmd.getContent();
        if (StringUtils.hasLength(content)) {
            List<String> ikSmartWord = esService.analyze(logRealTimeFilterCmd.getAnalyzer(), content);
            realTimeFilterRules.put("content#should", String.join("%", ikSmartWord));
        }
        String mqttClientId = logRealTimeFilterCmd.getMqttClientId();
        applicationContext.publishEvent(new LogAlarmRegisterEvent(this, mqttClientId, realTimeFilterRules));
        return Topic.builder()
                .topic(EasyLogConstants.LOG_AFTER_FILTERED_TOPIC + mqttClientId)
                .qos(1)
                .build();
    }

}
