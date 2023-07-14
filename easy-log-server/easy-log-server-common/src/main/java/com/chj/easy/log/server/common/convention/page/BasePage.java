package com.chj.easy.log.server.common.convention.page;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


/**
 * description 分页响应
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 22:06
 */
@Data
@NoArgsConstructor
public class BasePage<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    protected long total;

    /**
     * 结果集
     */
    protected List<T> list;

    public BasePage(List<T> list) {
        this.list = list;
        this.total = list.size();
    }

    public static <T> BasePage<T> of(List<T> list) {
        return new BasePage<T>(list);
    }
}
