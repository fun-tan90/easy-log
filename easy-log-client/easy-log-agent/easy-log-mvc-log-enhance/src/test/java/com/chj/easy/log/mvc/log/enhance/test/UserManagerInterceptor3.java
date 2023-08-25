package com.chj.easy.log.mvc.log.enhance.test;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/27 15:47
 */
public class UserManagerInterceptor3 {

    @RuntimeType
    public Object intercept(
            // 表示被拦截的目标对象，只有拦截实例方法时可用
            @This Object targetObj,
            // 被拦截的目标方法，只有拦截实例方法或静态方法时可用
            @Origin Method targetMethod,
            // 被拦截的目标方法参数
            @AllArguments Object[] targetArguments,
            // 表示被拦截的目标对象的父类对象，只有拦截实例方法时可用
            @Super Object superObj,
            //
            @SuperCall Callable<?> callable) throws Exception {
        Object res = null;
        try {
            System.out.println("before call");
            System.out.println("targetObj " + targetObj);
            System.out.println("targetMethod " + targetMethod);
            System.out.println("targetArguments " + Arrays.toString(targetArguments));
            System.out.println("superObj " + superObj);
            System.out.println("callable " + callable);
            res = callable.call(); // 执行原函数
            System.out.println("after call");
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            System.out.println("finally");
        }
        return res;
    }
}
