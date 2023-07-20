package com.chj.easy.log.admin.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/19 8:31
 */
@Data
@Builder
public class SysUserAuthVo {
    private String token;

    private String userId;

    private String userName;

    private List<String> roles;

    private List<String> permissions;

    private Map<String,String> extra;
}
