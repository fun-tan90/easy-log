package com.chj.easy.log.server;

import com.chj.easy.log.common.constant.EasyLogConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:12
 */
@EnableAsync
@SpringBootApplication(scanBasePackages = EasyLogConstants.SCAN_BASE_PACKAGES)
public class EasyLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyLogApplication.class, args);
    }

}
