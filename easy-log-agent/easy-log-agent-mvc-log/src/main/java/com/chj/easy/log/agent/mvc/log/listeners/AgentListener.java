package com.chj.easy.log.agent.mvc.log.listeners;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/28 8:27
 */
@Slf4j
public class AgentListener implements AgentBuilder.Listener {
    /**
     * 在提供给转换器的类型上调用。
     *
     * @param typeName    插入指令的类型的二进制名称。
     * @param classLoader 正在加载此类型的类加载器
     * @param module      检测类型的模块或{@code null}（如果当前VM不支持模块）。
     * @param loaded      {@code true} 如果类型已加载。
     */
    @Override
    public void onDiscovery(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
//        log.info("onDiscovery {}", typeName);
    }

    /**
     * 在成功应用转换之前调用。
     *
     * @param typeDescription 正在转换的类型。
     * @param classLoader     加载此类型的类加载器。
     * @param module          转换类型的模块或 {@code null}（如果当前VM不支持模块）。
     * @param loaded          {@code true} 如果类型已加载。
     * @param dynamicType     创建的动态类型。
     */
    @Override
    public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded, DynamicType dynamicType) {
        log.info("onTransformation {}", typeDescription.getActualName());
    }

    /**
     * 当类型未转换但被忽略时调用。
     *
     * @param typeDescription 转换时忽略的类型。
     * @param classLoader     加载此类型的类加载器。
     * @param module          如果当前VM不支持模块，则忽略类型的模块或{@code null}。
     * @param loaded          {@code true}如果类型已加载。
     */
    @Override
    public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule module, boolean loaded) {
//        log.info("onIgnored {}", typeDescription.getActualName());
    }

    /**
     * 在转换过程中发生错误时调用。
     *
     * @param typeName    插入指令的类型的二进制名称。
     * @param classLoader 加载此类型的类加载器。
     * @param module      插入指令的类型的模块或{@code null}（如果当前VM不支持模块）。
     * @param loaded      {@code true}如果类型已加载。
     * @param throwable   发生错误。
     */
    @Override
    public void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
        log.error("onError {}", typeName);
    }

    /**
     * 在尝试加载类之后调用，与类的处理无关。
     *
     * @param typeName    插入指令的类型的二进制名称。
     * @param classLoader 加载此类型的类加载器。
     * @param module      插入指令的类型的模块或{@code null}（如果当前VM不支持模块）。
     * @param loaded      {@code true}如果类型已加载。
     */
    @Override
    public void onComplete(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded) {
//        log.info("onComplete {}", typeName);
    }
}