package com.chj.easy.log.admin.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.log.SaLog;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.chj.easy.log.admin.property.EasyLogAdminProperties;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.convention.Res;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

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
@EnableScheduling
@EnableConfigurationProperties(EasyLogAdminProperties.class)
public class EasyLogAdminAutoConfiguration {

    // 注册 Sa-Token 全局过滤器
    @Bean
    public SaServletFilter saServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .addExclude("/favicon.ico", "/captcha", "/login")
                .setAuth(obj -> {
                    SaRouter.match("/**", StpUtil::checkLogin);
                })
                .setError(e -> {
                    SaHolder.getResponse().setHeader("Content-Type", "application/json;charset=UTF-8");
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
}