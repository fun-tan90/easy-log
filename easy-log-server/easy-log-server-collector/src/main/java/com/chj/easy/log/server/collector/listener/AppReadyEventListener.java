package com.chj.easy.log.server.collector.listener;

import com.chj.easy.log.common.constant.EasyLogConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.connection.stream.StreamInfo;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 10:15
 */
@Slf4j
@RequiredArgsConstructor
public class AppReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (initialized.compareAndSet(false, true)) {
            Boolean hasKey = stringRedisTemplate.hasKey(EasyLogConstants.STREAM_KEY);
            if (Boolean.FALSE.equals(hasKey)) {
                stringRedisTemplate.opsForStream().createGroup(EasyLogConstants.STREAM_KEY, EasyLogConstants.GROUP_NAME);
            } else {
                StreamInfo.XInfoGroups groups = stringRedisTemplate.opsForStream().groups(EasyLogConstants.STREAM_KEY);
                Optional<StreamInfo.XInfoGroup> xInfoGroupOpt = groups.stream().filter(n -> n.groupName().equals(EasyLogConstants.GROUP_NAME)).findAny();
                if (!xInfoGroupOpt.isPresent()) {
                    stringRedisTemplate.opsForStream().createGroup(EasyLogConstants.STREAM_KEY, EasyLogConstants.GROUP_NAME);
                }
            }
        }
    }
}
