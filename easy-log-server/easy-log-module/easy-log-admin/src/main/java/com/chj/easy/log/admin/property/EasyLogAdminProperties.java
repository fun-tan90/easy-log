package com.chj.easy.log.admin.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 10:31
 */
@Data
@ConfigurationProperties(prefix = EasyLogAdminProperties.PREFIX)
public class EasyLogAdminProperties {

    public static final String PREFIX = "easy-log.admin";

    private boolean enable = false;

    private boolean banner = true;

    private boolean auth = true;

    private boolean validateCaptcha = true;

    private String mqttWsAddress;

    private String username = "admin";

    private String password = "123456";
}
