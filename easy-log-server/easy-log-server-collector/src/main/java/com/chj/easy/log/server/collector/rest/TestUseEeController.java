package com.chj.easy.log.server.collector.rest;


import com.chj.easy.log.server.common.mapper.LogDocMapper;
import com.chj.easy.log.server.common.model.LogDoc;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试使用Easy-ES
 * <p>
 * Copyright © 2021 xpc1024 All Rights Reserved
 **/
@RestController
public class TestUseEeController {

    @Resource
    private LogDocMapper documentMapper;

    @GetMapping("/createIndex")
    public Boolean createIndex(String indexName) {
        // 1.初始化-> 创建索引(相当于mysql中的表)
        return documentMapper.createIndex();
    }

    @GetMapping("/insert")
    public Integer insert() {
        // 2.初始化-> 新增数据
        LogDoc logDoc = new LogDoc();
        logDoc.setCurrIp("172.16.8.31");
        logDoc.setContent("老汉推*技术过硬");
        return documentMapper.insert(logDoc);
    }

    @GetMapping("/search")
    public List<LogDoc> search() {
        // 3.查询出所有标题为老汉的文档列表
        LambdaEsQueryWrapper<LogDoc> wrapper = new LambdaEsQueryWrapper<>();
        wrapper.eq(LogDoc::getContent, "老汉");
        return documentMapper.selectList(wrapper);
    }
}
