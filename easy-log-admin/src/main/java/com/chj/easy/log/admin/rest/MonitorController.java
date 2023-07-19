package com.chj.easy.log.admin.rest;

import com.chj.easy.log.admin.model.vo.RedisStreamXInfoVo;
import com.chj.easy.log.admin.service.SysMonitorService;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.convention.Res;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/19 8:42
 */
@Slf4j
@RestController
@RequestMapping("monitor")
public class MonitorController {

    @Resource
    SysMonitorService sysMonitorService;

    @GetMapping("stream-x-info")
    public Res<RedisStreamXInfoVo> streamXInfo() {
        return Res.ok(sysMonitorService.streamXInfo(EasyLogConstants.STREAM_KEY));
    }
}