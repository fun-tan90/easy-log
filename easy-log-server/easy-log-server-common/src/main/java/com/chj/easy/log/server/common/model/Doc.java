package com.chj.easy.log.server.common.model;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/12 22:24
 */
public interface Doc {

    default String indexId() {
        return null;
    }

    String toSource();
}