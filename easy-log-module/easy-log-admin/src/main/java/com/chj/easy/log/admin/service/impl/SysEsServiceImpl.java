package com.chj.easy.log.admin.service.impl;

import com.chj.easy.log.admin.model.vo.EsIndexVo;
import com.chj.easy.log.admin.service.SysEsService;
import com.chj.easy.log.core.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/21 11:56
 */
@Slf4j
@Service
public class SysEsServiceImpl implements SysEsService {

    @Resource
    EsService esService;

    @Override
    public List<EsIndexVo> indexList(String indexNamePattern) {
        List<String> indexList = esService.indexQuery(indexNamePattern);
        return indexList.stream().map(n -> {
            String[] items = n.split(" ");
            List<String> properties = Arrays.stream(items).filter(StringUtils::hasLength).collect(Collectors.toList());
            return EsIndexVo.builder()
                    .health(properties.get(0))
                    .status(properties.get(1))
                    .index(properties.get(2))
                    .uuid(properties.get(3))
                    .pri(properties.get(4))
                    .rep(properties.get(5))
                    .docsCount(properties.get(6))
                    .docsDeleted(properties.get(7))
                    .storeSize(properties.get(8))
                    .priStoreSize(properties.get(9))
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public void barChart() {

    }
}
