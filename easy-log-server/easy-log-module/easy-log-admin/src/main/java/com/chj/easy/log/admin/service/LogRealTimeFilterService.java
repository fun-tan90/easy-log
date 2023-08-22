package com.chj.easy.log.admin.service;


import com.chj.easy.log.admin.model.cmd.LogRealTimeFilterCmd;
import com.chj.easy.log.core.model.Topic;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/6/12 22:00
 */
public interface LogRealTimeFilterService {

    Topic subscribe(LogRealTimeFilterCmd logRealTimeFilterCmd);

    void unsubscribe(String mqttClientId);

}
