package com.chj.easy.log.server.common.convention.enums;

import lombok.Getter;

/**
 * description
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/3/24 11:23
 */
@Getter
public enum LogicDelete {

    DELETE("1"),

    NORMAL("0");

    private final String value;

    LogicDelete(String value) {
        this.value = value;
    }
}
