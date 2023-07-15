package com.chj.easy.log.core.service.impl;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.core.model.LogDoc;
import com.chj.easy.log.core.service.AbstractEsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 13:09
 */
@Slf4j(topic = EasyLogConstants.LOG_TOPIC)
@Service
public class LogDocEsServiceImpl extends AbstractEsService<LogDoc> {

    @Override
    public void createIndexIfNotExists(String indexName) {
        boolean exists = exists(indexName);
        if (!exists) {
            String mappings = ResourceUtil.readUtf8Str(StrUtil.format(EasyLogConstants.INDEX_MAPPING_PATH, LogDoc.class.getSimpleName()));
            boolean createIndex = createIndex(indexName, mappings);
            log.debug("【{}】索引创建{}", indexName, createIndex ? "成功" : "失败");
        } else {
            log.debug("【{}】索引已存在", indexName);
        }
    }
}
