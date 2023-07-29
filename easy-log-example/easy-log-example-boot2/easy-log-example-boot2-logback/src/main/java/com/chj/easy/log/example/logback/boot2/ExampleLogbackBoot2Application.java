package com.chj.easy.log.example.logback.boot2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:12
 */
@SpringBootApplication(scanBasePackages = "com.chj.easy.log.example.boot2")
public class ExampleLogbackBoot2Application {

    public static void main(String[] args) {
        SpringApplication.run(ExampleLogbackBoot2Application.class, args);
    }

}
