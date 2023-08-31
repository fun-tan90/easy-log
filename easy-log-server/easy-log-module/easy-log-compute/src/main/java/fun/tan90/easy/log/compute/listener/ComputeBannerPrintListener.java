package fun.tan90.easy.log.compute.listener;

import fun.tan90.easy.log.common.BannerPrint;
import fun.tan90.easy.log.compute.property.EasyLogComputeProperties;
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
