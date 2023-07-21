package com.chj.easy.log.admin.model.vo;

import lombok.Builder;
import lombok.Data;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/21 22:20
 */
@Data
@Builder
public class SysUserMqttVo {

    private String mqttClientId;

    private String mqttUserName;

    private String mqttPassword;
}
