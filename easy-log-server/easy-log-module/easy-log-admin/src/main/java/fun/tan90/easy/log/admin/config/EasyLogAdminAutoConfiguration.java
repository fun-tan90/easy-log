package fun.tan90.easy.log.admin.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.log.SaLog;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import fun.tan90.easy.log.admin.config.stp.StpLogicForTokenPrefixCompatibleCookie;
import fun.tan90.easy.log.admin.property.EasyLogAdminProperties;
import fun.tan90.easy.log.common.constant.EasyLogConstants;
import fun.tan90.easy.log.core.convention.Res;
import fun.tan90.easy.log.core.convention.enums.IErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 8:50
 */
@Slf4j
@EnableAsync
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
    public StpLogicForTokenPrefixCompatibleCookie stpLogicForTokenPrefixCompatibleCookie() {
        return new StpLogicForTokenPrefixCompatibleCookie();
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