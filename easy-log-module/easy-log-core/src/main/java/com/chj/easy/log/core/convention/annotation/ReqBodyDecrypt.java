package com.chj.easy.log.core.convention.annotation;

import java.lang.annotation.*;

/**
 * description
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/1/31 14:02
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReqBodyDecrypt {
}
