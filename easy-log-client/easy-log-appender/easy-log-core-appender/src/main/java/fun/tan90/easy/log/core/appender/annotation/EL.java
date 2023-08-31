package fun.tan90.easy.log.core.appender.annotation;

import java.lang.annotation.*;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/4 22:03
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EL {

    /**
     * MDC的key
     *
     * @return
     */
    String key() default "";

    /**
     * MDC的值 或 spel
     *
     * @return
     */
    String value() default "";

}
