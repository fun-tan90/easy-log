package com.chj.easy.log.admin.model.cmd;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/17 15:55
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogQueryPageCmd extends BaseLogQueryCmd {

    private Integer pageNum = 1;

    private Integer pageSize = 500;
}
