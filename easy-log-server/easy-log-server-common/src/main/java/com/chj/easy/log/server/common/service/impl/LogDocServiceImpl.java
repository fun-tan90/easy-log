package com.chj.easy.log.server.common.service.impl;

import com.chj.easy.log.server.common.mapper.LogDocMapper;
import com.chj.easy.log.server.common.model.LogDoc;
import com.chj.easy.log.server.common.service.LogDocService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 11:00
 */
@Slf4j
@Service
public class LogDocServiceImpl implements LogDocService {

    @Resource
    LogDocMapper logDocMapper;

    @Override
    public void createIndex(String indexName) {
        if (Boolean.FALSE.equals(logDocMapper.existsIndex(indexName))) {
            Boolean createIndex = logDocMapper.createIndex(indexName);
            log.info("【{}】索引创建{}", indexName, Boolean.TRUE.equals(createIndex) ? "成功" : "失败");
        }
    }

    @Override
    public Integer insertBatch(List<LogDoc> entityList, String indexName) {
        return logDocMapper.insertBatch(entityList, LogDoc.indexName());
    }
}
