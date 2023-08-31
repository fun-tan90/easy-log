package fun.tan90.easy.log.admin.model.vo;

import fun.tan90.easy.log.core.model.Topic;
import lombok.Builder;
import lombok.Data;

import java.util.List;

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

    private String mqttWsAddress;

    private String mqttClientId;

    private String mqttUserName;

    private String mqttPassword;

    private List<Topic> subTopics;
}
