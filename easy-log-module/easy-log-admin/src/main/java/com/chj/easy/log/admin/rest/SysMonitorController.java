package com.chj.easy.log.admin.rest;

import com.chj.easy.log.admin.model.vo.RedisStreamXInfoVo;
import com.chj.easy.log.admin.service.SysMonitorService;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.convention.Res;
import com.chj.easy.log.core.convention.annotation.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/19 8:42
 */
@Slf4j
@RestController
@RequestMapping("sys/monitor")
public class SysMonitorController {

    @Resource
    SysMonitorService sysMonitorService;

    @GetMapping("x-info")
    @Log
    public Res<RedisStreamXInfoVo> streamXInfo() {
        return Res.ok(sysMonitorService.streamXInfo(EasyLogConstants.REDIS_STREAM_KEY));
    }

    @GetMapping("x-pending")
    public Res<Map<String, Map<String, List<String>>>> streamXPending() {
        return Res.ok(sysMonitorService.streamXPending(EasyLogConstants.REDIS_STREAM_KEY));
    }

    @PostMapping("x-ack")
    public Res<Long> streamXAck(String groupName, @RequestBody List<String> recordIds) {
        return Res.ok(sysMonitorService.streamXAck(EasyLogConstants.REDIS_STREAM_KEY, groupName, recordIds));
    }
}