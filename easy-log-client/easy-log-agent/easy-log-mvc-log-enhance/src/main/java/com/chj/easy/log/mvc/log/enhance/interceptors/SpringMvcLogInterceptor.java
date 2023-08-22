package com.chj.easy.log.mvc.log.enhance.interceptors;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/27 15:47
 */
@Slf4j
public class SpringMvcLogInterceptor {

    @RuntimeType
    public static Object intercept(@This Object targetObj,
                                   @Origin Method targetMethod,
                                   @AllArguments Object[] targetArguments,
                                   @SuperCall Callable<?> callable) throws Exception {
        long start = System.currentTimeMillis();
        Object res = null;
        try {
            res = callable.call(); // 执行原函数
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            long spendMs = System.currentTimeMillis() - start;
            String targetArgumentType = Arrays.stream(targetArguments).map(n -> {
                String simpleName = n.getClass().getSimpleName();
                return simpleName + " " + (n.getClass().isArray() ? StrUtil.lowerFirst(simpleName).replace("[]", "") : StrUtil.lowerFirst(simpleName));
            }).collect(Collectors.joining(", "));
            log.info("\n\n{}.{}({})->{}ms\nreq->{}\nres->{}\n", targetObj.getClass().getName(), targetMethod.getName(), targetArgumentType, spendMs, JSONUtil.toJsonStr(targetArguments.length == 1 ? targetArguments[0] : targetArguments), JSONUtil.toJsonStr(res));
        }
        return res;
    }
}
