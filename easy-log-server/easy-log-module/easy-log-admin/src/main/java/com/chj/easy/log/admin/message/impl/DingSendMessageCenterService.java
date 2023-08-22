package com.chj.easy.log.admin.message.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Singleton;
import com.chj.easy.log.admin.message.AbstractMessageCenterService;
import com.chj.easy.log.core.model.LogAlarmContent;
import com.dtflys.forest.Forest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/25 16:10
 */
@Slf4j
@Service
public class DingSendMessageCenterService extends AbstractMessageCenterService {

    private static final String MESSAGE_TEMPLATE = "message-template/ding.txt";

    private static final String MESSAGE_SERVER_URL = "https://oapi.dingtalk.com/robot/send?access_token=";

    @Override
    protected String alarmPlatformType() {
        return "ding";
    }

    @Override
    protected String buildMessageContent(LogAlarmContent logAlarmContent) {
        String robotTemplate = Singleton.get(MESSAGE_TEMPLATE, () -> ResourceUtil.readUtf8Str(MESSAGE_TEMPLATE));
        return String.format(robotTemplate,
                logAlarmContent.getAppName(),
                logAlarmContent.getNamespace(),
                logAlarmContent.getLoggerName(),
                DateUtil.format(new Date(logAlarmContent.getWindowStart()), DatePattern.NORM_DATETIME_PATTERN),
                DateUtil.format(new Date(logAlarmContent.getWindowEnd()), DatePattern.NORM_DATETIME_PATTERN),
                logAlarmContent.getThreshold(),
                logAlarmContent.getPeriod(),
                DateUtil.format(new Date(logAlarmContent.getWindowEnd()), DatePattern.NORM_DATETIME_PATTERN));
    }

    @Override
    protected void execute(String accessToken, String secret, String text, List<String> atMobiles) {
        String serverUrl = MESSAGE_SERVER_URL + accessToken;
        if (StringUtils.hasLength(secret)) {
            long timestamp = System.currentTimeMillis();
            String stringToSign = timestamp + "\n" + secret;
            try {
                Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
                byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
                String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), StandardCharsets.UTF_8.name());
                serverUrl = serverUrl + "&timestamp=" + timestamp + "&sign=" + sign;
            } catch (Exception ex) {
                log.error("Failed to sign the message sent by nailing.", ex);
            }
        }
        Map<String, String> markdown = new HashMap<>();
        markdown.put("title", "Easy-Log告警通知");
        markdown.put("text", text);

        Map<String, List<String>> at = new HashMap<>();
        at.put("atMobiles", atMobiles);

        String res = Forest.post(serverUrl)
                .addBody("msgtype", "markdown")
                .addBody("markdown", markdown)
                .addBody("at", at)
                .execute(String.class);
        log.info(res);
    }
}
