package com.chj.easy.log.admin.listener;


import cn.dev33.satoken.stp.StpInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * description 自定义权限验证接口扩展,权限认证时回调
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2022/6/1 13:11
 */
@Slf4j
@Component
public class StpInterfaceImpl implements StpInterface {

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        log.info("getPermissionList loginType {}, loginType {}", loginId, loginType);
        return Collections.singletonList("all");
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        log.info("getRoleList loginType {}, loginType {}", loginId, loginType);
        return Collections.singletonList("admin");
    }
}