package com.chj.easy.log.common.content;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/6/29 12:42
 */
@Setter
@Getter
public class EasyLogConfig implements Serializable {

    private static final long serialVersionUID = -6541180061782004705L;

    private String appName = "default";

    private String namespace = "default";

}
