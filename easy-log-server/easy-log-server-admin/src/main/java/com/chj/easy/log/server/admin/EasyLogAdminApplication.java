package com.chj.easy.log.server.admin;

import com.chj.easy.log.common.constant.EasyLogConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:12
 */
@SpringBootApplication(scanBasePackages = EasyLogConstants.SCAN_BASE_PACKAGES)
public class EasyLogAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyLogAdminApplication.class, args);
    }

}
