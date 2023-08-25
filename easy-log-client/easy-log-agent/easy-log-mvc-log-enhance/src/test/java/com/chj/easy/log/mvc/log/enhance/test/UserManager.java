package com.chj.easy.log.mvc.log.enhance.test;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/25 8:29
 */
public class UserManager {

    public UserManager() {
        System.out.println("UserManager 构造");
    }

    public String selectUserName(Long userId) {
        return "用户名称为" + userId;
    }

    public void printUser() {
        System.out.println("printUser 测试");
    }

    public int selectAge() {
        return 10;
    }
}
