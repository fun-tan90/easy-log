package com.chj.easy.log.admin.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.log.SaLog;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.admin.property.EasyLogAdminProperties;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.convention.Res;
import com.chj.easy.log.core.convention.enums.IErrorCode;
import com.github.benmanes.caffeine.cache.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:50
 */
@Slf4j
@ConditionalOnProperty(value = "easy-log.admin.enable", havingValue = "true")
@ComponentScan(EasyLogConstants.ADMIN_SCAN_BASE_PACKAGES)
@EnableConfigurationProperties({EasyLogAdminProperties.class})
public class EasyLogAdminAutoConfiguration {

    // 注册 Sa-Token 全局过滤器
    @Bean
    @ConditionalOnProperty(value = "easy-log.admin.auth", havingValue = "true")
    public SaServletFilter saServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .addExclude("/", "/_app.config.js", "/logo.png", "/resource/**", "/assets/**", "/favicon.ico", "/monitor/**", "/captcha", "/login")
                .setAuth(obj -> {
                    SaRouter.match("/**", StpUtil::checkLogin);
                })
                .setError(e -> {
                    SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
                    if (e instanceof NotLoginException) {
                        NotLoginException notLoginException = (NotLoginException) e;
                        String type = notLoginException.getType();
                        IErrorCode iErrorCode;
                        switch (type) {
                            case "-1": //未能读取到有效 token
                                iErrorCode = IErrorCode.AUTH_1001003;
                                break;
                            case "-2": //token 无效
                                iErrorCode = IErrorCode.AUTH_1001004;
                                break;
                            case "-3": //token 已过期
                                iErrorCode = IErrorCode.AUTH_1001005;
                                break;
                            case "-4": //token 已被顶下线
                                iErrorCode = IErrorCode.AUTH_1001006;
                                break;
                            case "-5": //token 已被踢下线
                                iErrorCode = IErrorCode.AUTH_1001007;
                                break;
                            case "-6": //token 已被冻结
                                iErrorCode = IErrorCode.AUTH_1001008;
                                break;
                            case "-7": //未按照指定前缀提交 token
                                iErrorCode = IErrorCode.AUTH_1001009;
                                break;
                            default:
                                iErrorCode = IErrorCode.AUTH_1001010;
                                break;
                        }
                        return JSONUtil.toJsonStr(Res.error(iErrorCode));
                    }
                    return JSONUtil.toJsonStr(Res.errorMsg(e.getMessage()));
                });
    }

    @Bean
    public SaLog saLog() {
        return new SaLog() {
            @Override
            public void trace(String str, Object... args) {
                log.trace(str, args);
            }

            @Override
            public void debug(String str, Object... args) {
                log.debug(str, args);
            }

            @Override
            public void info(String str, Object... args) {
                log.info(str, args);
            }

            @Override
            public void warn(String str, Object... args) {
                log.warn(str, args);
            }

            @Override
            public void error(String str, Object... args) {
                log.error(str, args);
            }

            @Override
            public void fatal(String str, Object... args) {
                log.error(str, args);
            }
        };
    }

    @Bean
    public LoadingCache<String, String> loadingCache(RemovalListener<String, String> removalListener, CacheLoader<String, String> cacheLoader) {
        return Caffeine.newBuilder()
                .scheduler(Scheduler.forScheduledExecutorService(Executors.newScheduledThreadPool(1)))
                .maximumSize(10_000)
                // 自上一次写入或者读取缓存开始，在经过指定时间之后过期。
                .expireAfterAccess(5, TimeUnit.SECONDS)
                // 自缓存生成后，经过指定时间或者一次替换值之后过期。
                .expireAfterWrite(15, TimeUnit.SECONDS)
                .refreshAfterWrite(10, TimeUnit.MINUTES)
                .recordStats()  // 记录统计信息
                .removalListener(removalListener)
                .build(cacheLoader);
    }
}