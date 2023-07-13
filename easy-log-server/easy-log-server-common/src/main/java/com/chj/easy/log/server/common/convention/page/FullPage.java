package com.chj.easy.log.server.common.convention.page;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * description 分页
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/5/31 20:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FullPage<U> extends SimplePage<U> {
    private Long totalPage;

    private boolean previous;

    private boolean next;

    public FullPage(int currentPage, long totalCount, List<U> records, Long totalPage, boolean previous, boolean next) {
        super(currentPage, totalCount, records);
        this.totalPage = totalPage;
        this.previous = previous;
        this.next = next;
    }
}