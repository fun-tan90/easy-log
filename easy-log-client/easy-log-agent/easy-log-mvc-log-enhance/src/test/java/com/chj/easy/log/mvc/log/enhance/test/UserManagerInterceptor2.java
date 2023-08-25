package com.chj.easy.log.mvc.log.enhance.test;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/27 15:47
 */
public class UserManagerInterceptor2 {

    public String selectUserName(Long userId) {
        return "用户名称2=>为" + userId;
    }
}
