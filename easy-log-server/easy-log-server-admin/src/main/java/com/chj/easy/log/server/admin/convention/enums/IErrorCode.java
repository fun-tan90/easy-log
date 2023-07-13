package com.chj.easy.log.server.admin.convention.enums;

import lombok.Getter;

/**
 * description
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/3/24 11:23
 */
@Getter
public enum IErrorCode {

    // 参考 https://www.kancloud.cn/onebase/ob/484204
    // 各个服务代码
    // ========== 系统级别 ==========
    OK("0", "ok"),
    SYS_2000001("2000001", null),
    SYS_2000002("2000002", "服务端发生异常，请联系管理员"),
    SYS_2000003("2000003", "解密异常"),
    SYS_2000004("2000004", "Netty网络异常，请联系管理员"),
    SYS_2000005("2000005", "请求头参数不正确"),
    SYS_2000006("2000006", "请求发起时间超过服务器限制"),
    SYS_2000007("2000007", "重复的请求"),
    SYS_2000008("2000008", "签名校验未通过"),
    SYS_2000009("2000009", "分布式锁获取失败,请稍后再试"),
    SYS_2000010("2000010", "获取HttpServletRequest对象异常"),
    SYS_2000011("2000011", "幂等Token已被使用或失效"),
    SYS_2000012("2000012", "头信息中缺少submit_token，无法重复性校验"),
    SYS_2000013("2000013", "请求过于频繁"),
    SYS_2000014("2000014", "请求过于频繁，已被禁止访问"),

    // ========== 业务级别 ==========
    AUTH_1001000("1001000", "用户尚未登录或登录已过期"),
    AUTH_1001001("1001001", "用户名或密码错误"),
    AUTH_1001002("1001002", "验证码尚未获取或已失效"),
    AUTH_1001003("1001003", "权限不足"),
    AUTH_1001004("1001004", "验证码不正确"),
    AUTH_1001005("1001005", "token已失效"),

    OSS_1002001("1002001", "文件上传失败"),

    ROLE_1003001("1003001", "角色代码已存在"),


    // ========== 客户端级别 ==========
    ;

    /**
     * 错误码
     */
    private final String code;
    /**
     * 错误提示
     */
    private final String message;

    IErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
