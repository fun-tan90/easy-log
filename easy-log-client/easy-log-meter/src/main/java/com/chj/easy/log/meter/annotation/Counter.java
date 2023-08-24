package com.chj.easy.log.meter.annotation;

import java.lang.annotation.*;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/24 9:01
 */
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Counter {
    String value() default "method.counted";

    Tag[] extraTags() default {};

    String description() default "";
}
