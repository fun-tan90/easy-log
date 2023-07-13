package com.chj.easy.log.server.admin.convention.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * description 分页
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/5/31 20:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimplePage<U> {
    private int currentPage;

    private long totalCount;

    private List<U> records;
}