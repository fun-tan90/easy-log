package com.chj.easy.log.mvc.log.enhance.test;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Morph;
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
        path = AgentTest.class.getClassLoader().getResource("").getPath();
        System.out.println("path = " + path);
    }

    /**
     * 方法插桩
     *
     * @throws IOException
     */
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

    /**
     * 注入方法
     *
     * @throws IOException
     */
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

    /**
     * 注入属性
     *
     * @throws IOException
     */
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

    /**
     * 方法委托1
     *
     * @throws Exception
     */
    @Test
    public void test4() throws Exception {
        DynamicType.Unloaded<UserManager> unloaded = new ByteBuddy()
                .subclass(UserManager.class)
                .name("a.b.SubUserManager4")
                .method(named("selectUserName"))
                .intercept(MethodDelegation.to(UserManagerInterceptor1.class))
                .make();

        DynamicType.Loaded<UserManager> load = unloaded.load(getClass().getClassLoader());
        Class<? extends UserManager> loaded = load.getLoaded();
        UserManager userManager = loaded.newInstance();
        System.out.println("userManager.selectUserName(1) = " + userManager.selectUserName(1L));
        unloaded.saveIn(new File(path));
    }

    /**
     * 方法委托2
     *
     * @throws Exception
     */
    @Test
    public void test5() throws Exception {
        DynamicType.Unloaded<UserManager> unloaded = new ByteBuddy()
                .subclass(UserManager.class)
                .name("a.b.SubUserManager5")
                .method(named("selectUserName"))
                .intercept(MethodDelegation.to(new UserManagerInterceptor2()))
                .make();

        DynamicType.Loaded<UserManager> load = unloaded.load(getClass().getClassLoader());
        Class<? extends UserManager> loaded = load.getLoaded();
        UserManager userManager = loaded.newInstance();
        System.out.println("userManager.selectUserName(2) = " + userManager.selectUserName(2L));
        unloaded.saveIn(new File(path));
    }

    /**
     * 方法委托3
     *
     * @throws Exception
     */
    @Test
    public void test6() throws Exception {
        DynamicType.Unloaded<UserManager> unloaded = new ByteBuddy()
                .subclass(UserManager.class)
                .name("a.b.SubUserManager6")
                .method(named("selectUserName"))
                .intercept(MethodDelegation.to(new UserManagerInterceptor3()))
                .make();

        DynamicType.Loaded<UserManager> load = unloaded.load(getClass().getClassLoader());
        Class<? extends UserManager> loaded = load.getLoaded();
        UserManager userManager = loaded.newInstance();
        System.out.println("userManager.selectUserName(3) = " + userManager.selectUserName(3L));

        unloaded.saveIn(new File(path));
    }


    /**
     * 动态修改入参
     *
     * @throws Exception
     */
    @Test
    public void test7() throws Exception {
        DynamicType.Unloaded<UserManager> unloaded = new ByteBuddy()
                .subclass(UserManager.class)
                .name("a.b.SubUserManager7")
                .method(named("selectUserName"))
                .intercept(MethodDelegation
                        .withDefaultConfiguration()
                        .withBinders(Morph.Binder.install(UserCallable.class))
                        .to(UserManagerInterceptor4.class))
                .make();

        DynamicType.Loaded<UserManager> load = unloaded.load(getClass().getClassLoader());
        Class<? extends UserManager> loaded = load.getLoaded();
        UserManager userManager = loaded.newInstance();
        System.out.println("userManager.selectUserName(7) = " + userManager.selectUserName(7L));

        unloaded.saveIn(new File(path));
    }
}
