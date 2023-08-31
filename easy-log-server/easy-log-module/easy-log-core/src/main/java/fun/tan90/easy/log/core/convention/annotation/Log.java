package fun.tan90.easy.log.core.convention.annotation;


import java.lang.annotation.*;

/**
 * description
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2022/9/22 22:16
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    boolean print() default true;

    boolean printReq() default true;

    boolean printRes() default true;
}