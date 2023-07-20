package com.chj.easy.log.core.convention.tree.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * 树父id
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface TreeFid {

    /**
     * 根元素父ID
     *
     * @return
     */
    String value() default "0";
}