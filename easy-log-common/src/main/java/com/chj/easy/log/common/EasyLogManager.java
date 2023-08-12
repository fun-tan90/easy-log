package com.chj.easy.log.common;


import com.chj.easy.log.common.content.EasyLogConfig;
import com.chj.easy.log.common.factory.EasyLogConfigFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/6/29 11:55
 */
public class EasyLogManager {

    public final static EasyLogConfig GLOBAL_CONFIG = EasyLogConfigFactory.createConfig();

    public final static AtomicLong SEQ = new AtomicLong(0);

}
