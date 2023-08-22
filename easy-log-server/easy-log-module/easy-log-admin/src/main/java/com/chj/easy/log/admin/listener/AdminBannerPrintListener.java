package com.chj.easy.log.admin.listener;

import com.chj.easy.log.admin.property.EasyLogAdminProperties;
import com.chj.easy.log.common.BannerPrint;
import com.chj.easy.log.common.constant.EasyLogConstants;
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
@Slf4j
@Component
public class AdminBannerPrintListener implements ApplicationListener<ContextRefreshedEvent> {

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    @Resource
    private EasyLogAdminProperties easyLogAdminProperties;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (initialized.compareAndSet(false, true)) {
            if (easyLogAdminProperties.isBanner()) {
                BannerPrint.printAdminBanner();
            }
        }
    }
}
