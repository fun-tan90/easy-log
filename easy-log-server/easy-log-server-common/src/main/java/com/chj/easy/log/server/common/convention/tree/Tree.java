package com.chj.easy.log.server.common.convention.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * description
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2022/06/14 22:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tree<T> {

    private T node;

    private List<Tree<T>> children;

}
