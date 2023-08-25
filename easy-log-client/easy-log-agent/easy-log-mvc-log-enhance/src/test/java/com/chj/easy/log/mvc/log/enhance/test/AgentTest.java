package com.chj.easy.log.mvc.log.enhance.test;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.FixedValue;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;

import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.returns;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/25 8:29
 */
public class AgentTest {
    private String path = null;

    @Before
    public void init() {
        path = AgentTest.class.getResource("").getPath();
        System.out.println("path = " + path);
    }

    @Test
    public void test1() throws IOException {
        DynamicType.Unloaded<UserManager> unloaded = new ByteBuddy()
                .redefine(UserManager.class)
                .name("a.b.SubUserManager1")
                .method(named("selectUserName")
                        .and(returns(TypeDescription.ForLoadedType.of(String.class))
                                .or(returns(TypeDescription.ForLoadedType.of(Class.class)))
                        ))
                .intercept(FixedValue.value("hello selectUserName"))

                .method(named("printUser")
                        .and(returns(TypeDescription.ForLoadedType.of(void.class))))
                .intercept(FixedValue.value(TypeDescription.ForLoadedType.of(void.class)))

                .method(named("selectAge"))
                .intercept(FixedValue.value(1223))
                .make();

        unloaded.saveIn(new File(path));
    }

    @Test
    public void test2() throws IOException {
        DynamicType.Unloaded<UserManager> unloaded = new ByteBuddy()
                .redefine(UserManager.class)
                .name("a.b.SubUserManager2")
                .defineMethod("hello", String.class, Modifier.PUBLIC + Modifier.STATIC)
                .withParameter(long.class, "id")
                .intercept(FixedValue.value("hello selectUserName"))
                .make();

        unloaded.saveIn(new File(path));
    }

    @Test
    public void test3() throws IOException {
        DynamicType.Unloaded<UserManager> unloaded = new ByteBuddy()
                .redefine(UserManager.class)
                .name("a.b.SubUserManager3")
                .defineField("age", int.class, Modifier.PRIVATE)
                .implement(AgeGetterSetterInterface.class)
                .intercept(FieldAccessor.ofField("age"))
                .make();
        unloaded.saveIn(new File(path));
    }

    @Test
    public void test4() throws IOException {
        DynamicType.Unloaded<UserManager> unloaded = new ByteBuddy()
                .redefine(UserManager.class)
                .name("a.b.SubUserManager4")
                .make();
        unloaded.saveIn(new File(path));
    }
}
