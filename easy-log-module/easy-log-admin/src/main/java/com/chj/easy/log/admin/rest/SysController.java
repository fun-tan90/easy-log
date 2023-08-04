package com.chj.easy.log.admin.rest;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.chj.easy.log.admin.model.vo.SysInfoVo;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.convention.Res;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 11:08
 */
@Slf4j
@RestController
@RequestMapping("sys")
public class SysController {

    @Resource
    BuildProperties buildProperties;

    @GetMapping("info")
    public Res<SysInfoVo> info() {
        return Res.ok(SysInfoVo.builder()
                .version(buildProperties.getVersion())
                .buildTime(DateUtil.format(new Date(Long.parseLong(buildProperties.get("time"))), DatePattern.NORM_DATETIME_PATTERN))
                .startUpTime(EasyLogConstants.EASY_LOG_START_UP_TIME)
                .build());
    }

}
