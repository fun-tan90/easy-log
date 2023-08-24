package com.chj.easy.log.mvc.log.enhance;

import com.chj.easy.log.mvc.log.enhance.listeners.AgentListener;
import com.chj.easy.log.mvc.log.enhance.transformers.AgentTransformer;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.annotation.AnnotationSource;
import net.bytebuddy.matcher.ElementMatcher;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/27 14:52
 */
@Slf4j
public class EasyLogAgent {

    public static void premain(String arg, Instrumentation instrumentation) {
        log.info("arg = {}", arg);
        new AgentBuilder
                .Default()
                // 忽略哪些类
                .ignore(ignoreType())
                // 根据包名前缀拦截类
                .type(type())
                // 拦截到的类由transformer处理
                .transform(new AgentTransformer())
                // 配置增强监听器
                .with(new AgentListener())
                // 安装到 Instrumentation
                .installOn(instrumentation);
    }

    private static ElementMatcher.Junction<AnnotationSource> type() {
        return isAnnotatedWith(
                named("org.springframework.web.bind.annotation.RestController")
                .or(named("org.springframework.stereotype.Controller"))
        );
    }

    private static ElementMatcher.Junction<NamedElement> ignoreType() {
        return nameStartsWith("net.bytebuddy.")
                .or(nameStartsWith("org.slf4j."))
                .or(nameStartsWith("org.groovy."))
                .or(nameStartsWith("org.springframework."))
                .or(nameStartsWith("sun.reflect"))
                .or(nameContains("javassist"))
                .or(nameContains(".asm."))
                .or(nameContains(".reflectasm."));
    }
}