package com.chj.easy.log.server.common.model;

import cn.hutool.json.JSONUtil;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/12 22:24
 */
public interface Doc {

    void setIndexId(String id);

    default String indexId() {
        return null;
    }

    default String toSource() {
        return JSONUtil.toJsonStr(this);
    }
}