package com.chj.easy.log.core.convention.annotation;

import java.lang.annotation.*;

/**
 * description controller方法上添加此注解用于 ResponseControllerAdvice 不起作用
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2022/8/17 8:33
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoResult {
}