package com.chj.easy.log.server.admin.convention.page;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈浩杰
 */

@Data
public class BasePageQuery {

    private Integer pageIndex = 1;

    private Integer pageSize = 10;

    private List<OrderColumn> orders = new ArrayList<>();

    private int totalRow = -1;

    @Data
    public static class OrderColumn {

        private String column;

        private boolean asc = true;
    }
}
