package com.chj.easy.log.admin.service;


import com.chj.easy.log.admin.model.cmd.SysUserLoginCmd;
import com.chj.easy.log.admin.model.vo.SysUserInfoVo;
import com.chj.easy.log.admin.model.vo.SysUserMqttVo;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/6/12 22:00
 */
public interface SysUserService {

    String basicAuth(SysUserLoginCmd sysUserLoginCmd);

    SysUserInfoVo userInfo();

    SysUserMqttVo userMqttInfo();
}
