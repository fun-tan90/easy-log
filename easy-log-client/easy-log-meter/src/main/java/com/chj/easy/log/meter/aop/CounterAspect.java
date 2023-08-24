package com.chj.easy.log.meter.aop;

import com.chj.easy.log.meter.MqttMeterRegistry;
import com.chj.easy.log.meter.annotation.Counter;
import com.chj.easy.log.meter.annotation.Tag;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/24 9:03
 */
@Aspect
@RequiredArgsConstructor
public class CounterAspect {

    private final MqttMeterRegistry mqttMeterRegistry;

    @Around("@annotation(counter)")
    public Object counter(ProceedingJoinPoint joinPoint, Counter counter) throws Throwable {
        try {
            Object proceed = joinPoint.proceed();
            record(joinPoint, counter, "success");
            return proceed;
        } catch (Throwable e) {
            record(joinPoint, counter, "failure");
            throw e;
        } finally {
            record(joinPoint, counter, "total");
        }
    }

    private void record(ProceedingJoinPoint joinPoint, Counter counter, String countWay) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String value = counter.value();
        Tag[] extraTags = counter.extraTags();
        String[] tags = new String[counter.extraTags().length * 2 + 8];
        for (int i = 0; i < counter.extraTags().length; i++) {
            tags[2 * i] = extraTags[i].tagKey();
            tags[2 * i + 1] = extraTags[i].tagValue();
        }
        tags[2 * counter.extraTags().length] = "methodName";
        tags[2 * counter.extraTags().length + 1] = joinPoint.getTarget().getClass().getName() + "." + method.getName();
        tags[2 * counter.extraTags().length + 2] = "parameterTypes";
        tags[2 * counter.extraTags().length + 3] = Arrays.stream(method.getParameterTypes()).map(Class::getName).collect(Collectors.joining(","));
        tags[2 * counter.extraTags().length + 4] = "returnType";
        tags[2 * counter.extraTags().length + 5] = method.getReturnType().getName();
        tags[2 * counter.extraTags().length + 6] = "countWay";
        tags[2 * counter.extraTags().length + 7] = countWay;
        io.micrometer.core.instrument.Counter.Builder builder =
                io.micrometer.core.instrument.Counter.builder(value).tags(tags);
        String description = counter.description();
        if (!description.isEmpty()) {
            builder.description(description);
        }
        builder.register(mqttMeterRegistry).increment();
    }
}
