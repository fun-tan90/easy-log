package com.chj.easy.log.event.service;



import com.chj.easy.log.event.model.ServerNode;

import java.util.List;


/**
 * description mqtt broker 服务
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/29 21:34
 */
public interface IMqttBrokerService {

	/**
	 * 获取服务节点
	 *
	 * @return 服务节点集合
	 */
	List<ServerNode> getNodes();

	/**
	 * 获取所有在线的客户端
	 *
	 * @return 在线数
	 */
	long getOnlineClientSize();

	/**
	 * 获取所有在线的客户端
	 *
	 * @return 客户端集合
	 */
	List<String> getOnlineClients();

	/**
	 * 获取所有在线的客户端
	 *
	 * @param nodeName 集群节点
	 * @return 在线数
	 */
	long getOnlineClientSize(String nodeName);

	/**
	 * 获取所有在线的客户端
	 *
	 * @param nodeName 集群节点
	 * @return 客户端集合
	 */
	List<String> getOnlineClients(String nodeName);

}
