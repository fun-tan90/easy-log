/*
 * Copyright 2020-2099 sa-token.cc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chj.easy.log.common.factory;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.chj.easy.log.common.content.EasyLogConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * description 配置文件的构建工厂类
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/6/29 13:10
 */
public class EasyLogConfigFactory {

    private EasyLogConfigFactory() {
    }

    /**
     * 配置文件地址
     */
    public static String configPath = "easy-log.properties";

    /**
     * 根据 configPath 路径获取配置信息
     *
     * @return 一个MetricCatcherConfig对象
     */
    public static EasyLogConfig createConfig() {
        return createConfig(configPath);
    }

    /**
     * 根据指定路径路径获取配置信息
     *
     * @param path 配置文件路径
     * @return 一个 MetricCatcherConfig 对象
     */
    public static EasyLogConfig createConfig(String path) {
        Map<String, String> map = readPropToMap(path);
        return BeanUtil.mapToBean(map, EasyLogConfig.class, true, new CopyOptions());
    }

    /**
     * 工具方法: 将指定路径的properties配置文件读取到Map中
     *
     * @param propertiesPath 配置文件地址
     * @return 一个Map
     */
    private static Map<String, String> readPropToMap(String propertiesPath) {
        Map<String, String> map = new HashMap<>(16);
        try {
            InputStream is = EasyLogConfigFactory.class.getClassLoader().getResourceAsStream(propertiesPath);
            if (is == null) {
                return null;
            }
            Properties prop = new Properties();
            prop.load(is);
            for (String key : prop.stringPropertyNames()) {
                map.put(key, prop.getProperty(key));
            }
        } catch (IOException e) {
            throw new RuntimeException("配置文件(" + propertiesPath + ")加载失败", e);
        }
        return map;
    }
}
