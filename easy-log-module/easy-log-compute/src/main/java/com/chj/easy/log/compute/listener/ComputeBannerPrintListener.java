package com.chj.easy.log.compute.listener;

import com.chj.easy.log.common.BannerPrint;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.compute.property.EasyLogComputeProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 10:15
 */
@Slf4j(topic = EasyLogConstants.EASY_LOG_TOPIC)
@Component
public class ComputeBannerPrintListener implements ApplicationListener<ContextRefreshedEvent> {

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    @Resource
    private EasyLogComputeProperties easyLogComputeProperties;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (initialized.compareAndSet(false, true)) {
            if (easyLogComputeProperties.isBanner()) {
                BannerPrint.printComputeBanner();
            }
        }
    }
}
