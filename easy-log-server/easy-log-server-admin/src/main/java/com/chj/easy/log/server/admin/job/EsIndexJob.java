package com.chj.easy.log.server.admin.job;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.chj.easy.log.server.common.mapper.LogDocMapper;
import com.chj.easy.log.server.common.model.LogDoc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 17:34
 */
@Slf4j
@Component
public class EsIndexJob {

    @Resource
    LogDocMapper logDocMapper;

    @Scheduled(cron = "0/5 * * * * ?")
    public void initIndex() {
        String indexName = LogDoc.indexName();
        if (Boolean.FALSE.equals(logDocMapper.existsIndex(indexName))) {
            Boolean createIndex = logDocMapper.createIndex(indexName);
            log.info("【{}】索引创建{}", indexName, Boolean.TRUE.equals(createIndex) ? "成功" : "失败");
        }
    }
}
