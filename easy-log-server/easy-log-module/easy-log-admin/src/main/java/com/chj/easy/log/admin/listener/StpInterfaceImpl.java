package com.chj.easy.log.admin.listener;


import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.session.SaSessionCustomUtil;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
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
        log.debug("GetPermissionList loginType {}, loginType {}", loginId, loginType);
        // 1. 声明权限码集合
        List<String> permissionList = new ArrayList<>();
        // 2. 遍历角色列表，查询拥有的权限码
        for (String roleId : getRoleList(loginId, loginType)) {
            SaSession roleSession = SaSessionCustomUtil.getSessionById("role-" + roleId);
            List<String> list = roleSession.get("permission_list", () -> {
                // 从数据库查询这个角色所拥有的权限列表
                return Arrays.asList("add", "edit", "delete");
            });
            permissionList.addAll(list);
        }
        // 3. 返回权限码集合
        return permissionList;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        log.debug("GetRoleList loginType {}, loginType {}", loginId, loginType);
        SaSession session = StpUtil.getSessionByLoginId(loginId);
        return session.get("role_list", () -> {
            // 从数据库查询这个账号id拥有的角色列表
            return Collections.singletonList("admin");
        });
    }
}