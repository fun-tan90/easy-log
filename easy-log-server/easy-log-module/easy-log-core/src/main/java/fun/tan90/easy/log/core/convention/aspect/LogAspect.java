package fun.tan90.easy.log.core.convention.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.SystemClock;
import cn.hutool.json.JSONUtil;
import fun.tan90.easy.log.core.convention.annotation.Log;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * description
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2022/9/22 12:40
 */
@Slf4j
@Aspect
@Order(1)
public class LogAspect {

    @Pointcut("@annotation(fun.tan90.easy.log.core.convention.annotation.Log) || @within(org.springframework.web.bind.annotation.RestController)")
    public void logAround() {
    }

    @Around("logAround()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = SystemClock.now();
        String beginTime = DateUtil.now();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object result = null;
        try {
            result = joinPoint.proceed();
        } finally {
            Method method = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(), methodSignature.getMethod().getParameterTypes());
            Log logAnnotation = Optional.ofNullable(method.getAnnotation(Log.class)).orElse(joinPoint.getTarget().getClass().getAnnotation(Log.class));
            if (logAnnotation != null && logAnnotation.print()) {
                LogPrint logPrint = new LogPrint();
                logPrint.setBeginTime(beginTime);
                if (logAnnotation.printReq()) {
                    logPrint.setReq(buildInput(joinPoint));
                }
                if (logAnnotation.printRes()) {
                    logPrint.setRes(result);
                }
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                String methodType = Optional.ofNullable(requestAttributes).map(req -> req.getRequest().getMethod()).orElse("unknown");
                String requestUri = Optional.ofNullable(requestAttributes).map(req -> req.getRequest().getRequestURI()).orElse("unknown");
                log.info("[{}] {}, executeTime: {}ms\n{}", methodType, requestUri, SystemClock.now() - startTime, JSONUtil.toJsonPrettyStr(logPrint));
            }
        }
        return result;
    }

    private Object[] buildInput(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Object[] printArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if ((args[i] instanceof HttpServletRequest) || args[i] instanceof HttpServletResponse) {
                continue;
            }
            if (args[i] instanceof byte[]) {
                printArgs[i] = "byte array";
            } else if (args[i] instanceof MultipartFile) {
                printArgs[i] = "file";
            } else {
                printArgs[i] = args[i];
            }
        }
        return printArgs;
    }

    @Data
    private static class LogPrint {

        private String beginTime;

        private Object[] req;

        private Object res;
    }
}
