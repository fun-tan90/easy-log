package com.chj.easy.log.admin.service;


import com.chj.easy.log.admin.model.SysUserLoginCmd;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/6/12 22:00
 */
public interface SysUserService {

    String basicAuth(SysUserLoginCmd sysUserLoginCmd);

}
