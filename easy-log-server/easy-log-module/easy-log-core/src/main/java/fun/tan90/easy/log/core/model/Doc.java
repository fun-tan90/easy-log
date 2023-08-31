package fun.tan90.easy.log.core.model;

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

    String indexId();

    default String toSource() {
        return JSONUtil.toJsonStr(this);
    }
}