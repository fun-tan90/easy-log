package com.chj.easy.log.admin.mqtt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * description mqtt 服务节点
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/29 21:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerNode {

    /**
     * 节点名称
     */
    private String name;
    /**
     * ip:port
     */
    private String peerHost;

}
