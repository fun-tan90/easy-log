package com.chj.easy.log.server.rest;

import com.chj.easy.log.common.constant.EasyLogConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author chj
 * @date 2021年07月17日 16:38
 */
@Slf4j
@Controller
public class IndexController {

    @Value("${easy-log.admin.enable:true}")
    private boolean adminEnable;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("notify", "您当前使用的是Easy-log日志收集模块，无法使用日志检索等功能，当前版本为" + EasyLogConstants.EASY_LOG_VERSION);
        return adminEnable ? "index" : "notify";
    }
}