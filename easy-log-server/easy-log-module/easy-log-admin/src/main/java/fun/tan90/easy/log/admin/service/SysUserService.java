package fun.tan90.easy.log.admin.service;


import fun.tan90.easy.log.admin.model.cmd.SysUserLoginCmd;
import fun.tan90.easy.log.admin.model.vo.SysUserInfoVo;
import fun.tan90.easy.log.admin.model.vo.SysUserMqttVo;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/6/12 22:00
 */
public interface SysUserService {

    /**
     * 认证
     * @param sysUserLoginCmd
     * @return
     */
    String basicAuth(SysUserLoginCmd sysUserLoginCmd);

    /**
     * 用户基础信息
     * @return
     */
    SysUserInfoVo userInfo();

    /**
     * 当前用户mqtt连接信息
     * @return
     */
    SysUserMqttVo userMqttInfo();
}
