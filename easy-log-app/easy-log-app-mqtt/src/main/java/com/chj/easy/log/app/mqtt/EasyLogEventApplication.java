package com.chj.easy.log.app.mqtt;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:12
 */
@EnableAdminServer
@SpringBootApplication
public class EasyLogEventApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyLogEventApplication.class, args);
    }

}
