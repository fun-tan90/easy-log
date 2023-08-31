package fun.tan90.easy.log.mvc.log.enhance.transformers;

import fun.tan90.easy.log.mvc.log.enhance.interceptors.SpringMvcLogInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/28 8:18
 */
@Slf4j
public class AgentTransformer implements AgentBuilder.Transformer {
    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                            TypeDescription typeDescription,
                                            ClassLoader classLoader,
                                            JavaModule module,
                                            ProtectionDomain protectionDomain) {
        return builder.method(not(isStatic())
                        .and(isAnnotatedWith(nameStartsWith("org.springframework.web.bind.annotation").and(nameEndsWith("Mapping")))))
                .intercept(MethodDelegation.to(SpringMvcLogInterceptor.class));
    }
}