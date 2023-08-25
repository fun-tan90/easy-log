package com.chj.easy.log.mvc.log.enhance.test;

import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/27 15:47
 */
public class UserManagerInterceptor4 {

    @RuntimeType
    public static Object intercept(
            // 表示被拦截的目标对象，只有拦截实例方法时可用
            @This Object targetObj,
            // 被拦截的目标方法，只有拦截实例方法或静态方法时可用
            @Origin Method targetMethod,
            // 被拦截的目标方法参数
            @AllArguments Object[] targetArguments,
            // 表示被拦截的目标对象的父类对象，只有拦截实例方法时可用
            @Super Object superObj,
            @Morph UserCallable userCallable
    ) throws Exception {
        Object res = null;
        try {
            System.out.println("before call");
            if (targetArguments != null && targetArguments.length > 0) {
                targetArguments[0] = Long.parseLong(targetArguments[0].toString()) * 2;
            }
            res = userCallable.call(targetArguments); // 执行原函数
            System.out.println("after call");
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            System.out.println("finally");
        }
        return res;
    }
}
