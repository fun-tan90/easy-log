package fun.tan90.easy.log.core.indicator;

import net.dreamlu.iot.mqtt.core.client.MqttClientCreator;
import net.dreamlu.iot.mqtt.spring.client.MqttClientTemplate;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import javax.annotation.Resource;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/3 13:19
 */
public class MqttClientHealthIndicator implements HealthIndicator {

    @Resource
    MqttClientTemplate mqttClientTemplate;

    @Override
    public Health health() {
        if (mqttClientTemplate.isConnected()) {
            MqttClientCreator clientCreator = mqttClientTemplate.getClientCreator();
            return Health.up()
                    .withDetail("clientId", clientCreator.getClientId())
                    .withDetail("brokerIp", clientCreator.getIp())
                    .withDetail("brokerPort", clientCreator.getPort())
                    .withDetail("version", clientCreator.getVersion())
                    .withDetail("keepAliveSecs", clientCreator.getKeepAliveSecs())
                    .withDetail("reconnect", clientCreator.isReconnect())
                    .withDetail("cleanSession", clientCreator.isCleanSession())
                    .build();
        }
        return Health.down().build();
    }
}
