package com.chj.easy.log.admin.message;

import cn.hutool.extra.spring.SpringUtil;
import com.chj.easy.log.core.model.LogAlarmContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * description 策略选择器
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2022/6/20 9:37
 */
@Slf4j
@Component
public class MessageCenterServiceChoose implements ApplicationListener<ContextRefreshedEvent> {

    private boolean executeOnlyOnce = true;

    private final Map<String, AbstractMessageCenterService> abstractExecuteStrategyMap = new HashMap<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        synchronized (MessageCenterServiceChoose.class) {
            if (executeOnlyOnce) {
                executeOnlyOnce = false;
                Map<String, AbstractMessageCenterService> actual = SpringUtil.getBeansOfType(AbstractMessageCenterService.class);
                actual.forEach((beanName, bean) -> {
                    AbstractMessageCenterService beanExist = abstractExecuteStrategyMap.get(bean.alarmPlatformType());
                    if (beanExist != null) {
                        throw new RuntimeException(String.format("[%s] Duplicate execution policy", bean.alarmPlatformType()));
                    }
                    abstractExecuteStrategyMap.put(bean.alarmPlatformType(), bean);
                });
            }
        }
    }

    private AbstractMessageCenterService choose(String alarmPlatformType) {
        return Optional.ofNullable(abstractExecuteStrategyMap.get(alarmPlatformType)).orElseThrow(() -> new RuntimeException(String.format("[%s] 未知的消息推送类型", alarmPlatformType)));
    }

    public final void execute(LogAlarmContent logAlarmContent) {
        if (Objects.isNull(logAlarmContent)) {
            return;
        }
        AbstractMessageCenterService executeStrategy = choose(logAlarmContent.getAlarmPlatformType());
        executeStrategy.sendAlarmMessage(logAlarmContent);
    }
}
