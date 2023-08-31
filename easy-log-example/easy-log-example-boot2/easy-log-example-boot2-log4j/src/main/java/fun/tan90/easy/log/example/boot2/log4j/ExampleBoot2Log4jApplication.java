package fun.tan90.easy.log.example.boot2.log4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:12
 */
@SpringBootApplication(scanBasePackages = "fun.tan90.easy.log.example.boot2")
public class ExampleBoot2Log4jApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleBoot2Log4jApplication.class, args);
    }

}
