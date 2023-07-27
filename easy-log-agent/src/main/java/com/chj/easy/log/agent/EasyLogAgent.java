package com.chj.easy.log.agent;

import com.chj.easy.log.agent.interceptors.TimeInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.lang.instrument.Instrumentation;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/27 14:52
 */
@Slf4j
public class EasyLogAgent {

    /**
     * 该方法在main方法之前运行，与main方法运行在同一个JVM中
     */
    public static void premain(String arg, Instrumentation instrumentation) {
        log.info("agent的premain(String arg={}, Instrumentation instrumentation)方法", arg);
        // Byte Buddy会根据 Transformer指定的规则进行拦截并增强代码
        AgentBuilder.Transformer transformer =
                (builder, typeDescription, classLoader, javaModule, protectionDomain) -> {
                    // method()指定哪些方法需要被拦截，ElementMatchers.any()表
                    // 示拦截所有方法
                    return builder.method(ElementMatchers.any())
                            // intercept()指明拦截上述方法的拦截器
                            .intercept(MethodDelegation.to(TimeInterceptor.class));
                };

        // 创建一个过滤规则
        AgentBuilder.Listener listener = new AgentBuilder.Listener() {
            /**
             * 在提供给转换器的类型上调用。
             *
             * @param typeName      插入指令的类型的二进制名称。
             * @param classLoader   正在加载此类型的类加载器
             * @param module        检测类型的模块或{@code null}（如果当前VM不支持模块）。
             * @param loaded        {@code true} 如果类型已加载。
             */
            @Override
            public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {

            }

            /**
             * 在成功应用转换之前调用。
             *
             * @param typeDescription   正在转换的类型。
             * @param classLoader       加载此类型的类加载器。
             * @param module            转换类型的模块或 {@code null}（如果当前VM不支持模块）。
             * @param loaded            {@code true} 如果类型已加载。
             * @param dynamicType       创建的动态类型。
             */
            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {

            }

            /**
             * 当类型未转换但被忽略时调用。
             *
             * @param typeDescription    转换时忽略的类型。
             * @param classLoader        加载此类型的类加载器。
             * @param module             如果当前VM不支持模块，则忽略类型的模块或{@code null}。
             * @param loaded             {@code true}如果类型已加载。
             */
            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {

            }

            /**
             * 在转换过程中发生错误时调用。
             *
             * @param typeName      插入指令的类型的二进制名称。
             * @param classLoader   加载此类型的类加载器。
             * @param module        插入指令的类型的模块或{@code null}（如果当前VM不支持模块）。
             * @param loaded        {@code true}如果类型已加载。
             * @param throwable     发生错误。
             */
            @Override
            public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {

            }

            /**
             * 在尝试加载类之后调用，与类的处理无关。
             *
             * @param typeName      插入指令的类型的二进制名称。
             * @param classLoader   加载此类型的类加载器。
             * @param module        插入指令的类型的模块或{@code null}（如果当前VM不支持模块）。
             * @param loaded        {@code true}如果类型已加载。
             */
            @Override
            public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {

            }
        };

        // Byte Buddy专门有个AgentBuilder来处理Java Agent的场景
        new AgentBuilder
                .Default()
                // 根据包名前缀拦截类
                .type(ElementMatchers.nameStartsWith("com.chj.easy.log"))
                // 拦截到的类由transformer处理
                .transform(transformer)
                .with(listener)
                .installOn(instrumentation); // 安装到 Instrumentation
    }

    /**
     * 若不存在 premain(String agentArgs, Instrumentation inst)，
     * 则会执行 premain(String agentArgs)
     */
    public static void premain(String arg) {
        System.out.println("agent的premain(String arg)方法");
    }
}