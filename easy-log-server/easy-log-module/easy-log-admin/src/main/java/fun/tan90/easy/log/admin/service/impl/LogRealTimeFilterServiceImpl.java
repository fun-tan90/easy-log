package fun.tan90.easy.log.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import fun.tan90.easy.log.admin.model.cmd.LogRealTimeFilterCmd;
import fun.tan90.easy.log.admin.service.LogRealTimeFilterService;
import fun.tan90.easy.log.common.constant.EasyLogConstants;
import fun.tan90.easy.log.core.model.LogRealTimeFilterRule;
import fun.tan90.easy.log.core.model.Topic;
import fun.tan90.easy.log.core.service.CacheService;
import fun.tan90.easy.log.core.service.EsService;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.iot.mqtt.codec.MqttQoS;
import net.dreamlu.iot.mqtt.spring.client.MqttClientTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/18 16:21
 */
@Slf4j
@Service
public class LogRealTimeFilterServiceImpl implements LogRealTimeFilterService {

    @Resource
    MqttClientTemplate mqttClientTemplate;

    @Resource
    CacheService cacheService;

    @Resource
    EsService esService;

    @Override
    public Topic subscribe(LogRealTimeFilterCmd logRealTimeFilterCmd) {
        List<String> sqlItem = new ArrayList<>();
        List<String> colList = logRealTimeFilterCmd.getColList();
        String select = "select " + String.join(", ", colList) + " from test where ";
        String whereCondition = logRealTimeFilterCmd.getWhereCondition();
        if (!StringUtils.hasLength(whereCondition)) {
            long timestamp = System.currentTimeMillis();
            sqlItem.add(StrUtil.format("(timeStamp >= '{}')", timestamp));
            sqlItem.add(StrUtil.format("(namespace = '{}')", logRealTimeFilterCmd.getNamespace()));

            List<String> appNameList = logRealTimeFilterCmd.getAppNameList();
            if (!CollectionUtils.isEmpty(appNameList)) {
                sqlItem.add(StrUtil.format("(appName in ({}))", appNameList.stream().map(n -> StrUtil.format("'{}'", n)).collect(Collectors.joining(", "))));
            }
            List<String> levelList = logRealTimeFilterCmd.getLevelList();
            if (!CollectionUtils.isEmpty(levelList)) {
                sqlItem.add(StrUtil.format("(level in ({}))", levelList.stream().map(n -> StrUtil.format("'{}'", n)).collect(Collectors.joining(", "))));
            }
            String loggerName = logRealTimeFilterCmd.getLoggerName();
            if (StringUtils.hasLength(loggerName)) {
                sqlItem.add(StrUtil.format("(loggerName like '%{}%')", loggerName));
            }
            String lineNumber = logRealTimeFilterCmd.getLineNumber();
            if (StringUtils.hasLength(lineNumber)) {
                sqlItem.add(StrUtil.format("(lineNumber = '{}')", lineNumber));
            }
            List<String> ipList = logRealTimeFilterCmd.getIpList();
            if (!CollectionUtils.isEmpty(ipList)) {
                sqlItem.add(StrUtil.format("(currIp in ({}))", ipList.stream().map(n -> StrUtil.format("'{}'", n)).collect(Collectors.joining(", "))));
            }
            String content = logRealTimeFilterCmd.getContent();
            if (StringUtils.hasLength(content)) {
                List<String> ikSmartWord = esService.analyze(logRealTimeFilterCmd.getAnalyzer(), content);
                String contentSql = ikSmartWord.stream().map(n -> StrUtil.format("content like '%{}%'", n)).collect(Collectors.joining("or "));
                sqlItem.add(StrUtil.format("({})", contentSql));
            }
            whereCondition = String.join(" and ", sqlItem);
        }
        String sql = select + whereCondition;
        log.info(sql);
        String mqttClientId = logRealTimeFilterCmd.getMqttClientId();
        LogRealTimeFilterRule logRealTimeFilterRule = LogRealTimeFilterRule.builder()
                .clientId(mqttClientId)
                .sql(sql)
                .namespace(logRealTimeFilterCmd.getNamespace())
                .appNameList(logRealTimeFilterCmd.getAppNameList())
                .levelList(logRealTimeFilterCmd.getLevelList())
                .loggerName(logRealTimeFilterCmd.getLoggerName())
                .lineNumber(logRealTimeFilterCmd.getLineNumber())
                .ipList(logRealTimeFilterCmd.getIpList())
                .analyzer(logRealTimeFilterCmd.getAnalyzer())
                .content(logRealTimeFilterCmd.getContent())
                .colList(logRealTimeFilterCmd.getColList())
                .whereCondition(logRealTimeFilterCmd.getWhereCondition())
                .build();
        cacheService.addLogRealTimeFilterRule(logRealTimeFilterRule);
        mqttClientTemplate.publish(EasyLogConstants.LOG_REAL_TIME_FILTER_RULES_TOPIC + "put", JSONUtil.toJsonStr(logRealTimeFilterRule).getBytes(StandardCharsets.UTF_8), MqttQoS.EXACTLY_ONCE);
        return Topic.builder()
                .topic(EasyLogConstants.MQTT_LOG_AFTER_FILTERED_TOPIC + mqttClientId)
                .qos(1)
                .build();
    }

    @Override
    public void unsubscribe(String mqttClientId) {
        cacheService.delLogRealTimeFilterRule(mqttClientId);
        mqttClientTemplate.publish(EasyLogConstants.LOG_REAL_TIME_FILTER_RULES_TOPIC + "remove", mqttClientId.getBytes(StandardCharsets.UTF_8), MqttQoS.EXACTLY_ONCE);
    }

}
