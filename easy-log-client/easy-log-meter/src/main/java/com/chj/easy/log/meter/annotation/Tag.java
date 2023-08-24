package com.chj.easy.log.meter.annotation;

import java.lang.annotation.*;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/24 9:11
 */
@Inherited
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Tag {

    String tagKey() default "";

    String tagValue() default "";
}