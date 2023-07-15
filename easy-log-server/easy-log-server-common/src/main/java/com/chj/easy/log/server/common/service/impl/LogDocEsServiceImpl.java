package com.chj.easy.log.server.common.service.impl;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import com.chj.easy.log.common.constant.EasyLogConstants;
import com.chj.easy.log.server.common.model.LogDoc;
import com.chj.easy.log.server.common.service.AbstractEsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 13:09
 */
@Slf4j
@Service
public class LogDocEsServiceImpl extends AbstractEsService<LogDoc> {

    @Override
    public void createIndexIfNotExists(String indexName) {
        boolean exists = exists(indexName);
        if (!exists) {
            String mappings = ResourceUtil.readUtf8Str(StrUtil.format(EasyLogConstants.INDEX_MAPPING_PATH, LogDoc.class.getSimpleName()));
            boolean createIndex = createIndex(indexName, mappings);
            log.info("【{}】索引创建{}", indexName, createIndex ? "成功" : "失败");
        } else {
            log.info("【{}】索引已存在", indexName);
        }
    }
}
