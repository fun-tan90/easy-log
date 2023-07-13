package com.chj.easy.log.server.admin.convention.tree;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.chj.easy.log.server.admin.convention.tree.annotation.TreeFid;
import com.chj.easy.log.server.admin.convention.tree.annotation.TreeId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * description
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2022/5/10 16:16
 */
public class TreeConvert<T> {

    public List<Tree<T>> tree(List<T> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return new ArrayList<>();
        }
        String idName;
        String fIdName;
        Field[] fields = ReflectUtil.getFields(dataList.get(0).getClass());
        Optional<Field> treeIdOpt = Arrays.stream(fields).filter(n -> n.getAnnotation(TreeId.class) != null).findFirst();
        Optional<Field> treeFidOpt = Arrays.stream(fields).filter(n -> n.getAnnotation(TreeFid.class) != null).findFirst();
        if (treeIdOpt.isPresent() && treeFidOpt.isPresent()) {
            idName = ReflectUtil.getFieldName(treeIdOpt.get());
            fIdName = ReflectUtil.getFieldName(treeFidOpt.get());
            String fId = treeFidOpt.get().getAnnotation(TreeFid.class).value();
            List<Tree<T>> parentList = new ArrayList<>();
            List<Tree<T>> childList = new ArrayList<>();
            for (T t : dataList) {
                Object fieldValue = ReflectUtil.getFieldValue(t, fIdName);
                if (StrUtil.equals(fId, String.valueOf(fieldValue))) {
                    parentList.add(new Tree<>(t, new ArrayList<>()));
                } else {
                    childList.add(new Tree<>(t, new ArrayList<>()));
                }
            }
            recursionChildren(parentList, childList, idName, fIdName);
            return parentList;
        } else {
            throw new IllegalArgumentException("TreeId 或 TreeFid 不能为空");
        }
    }

    /**
     * 递归获取子节点数据
     *
     * @param parentList
     * @param childList
     * @param idName
     * @param fIdName
     */
    public void recursionChildren(List<Tree<T>> parentList, List<Tree<T>> childList,
                                  String idName, String fIdName) {
        for (Tree<T> parentMap : parentList) {
            Object idFieldValue = ReflectUtil.getFieldValue(parentMap.getNode(), idName);
            List<Tree<T>> childrenList = new ArrayList<>();
            for (Tree<T> allMap : childList) {
                Object fIdFieldValue = ReflectUtil.getFieldValue(allMap.getNode(), fIdName);
                if (StrUtil.equals(String.valueOf(idFieldValue), String.valueOf(fIdFieldValue))) {
                    childrenList.add(allMap);
                }
            }
            if (!childrenList.isEmpty()) {
                childList.removeAll(childrenList);
                parentMap.setChildren(childrenList);
                recursionChildren(childrenList, childList, idName, fIdName);
            }
        }
    }

    public static void main(String[] args) {
        List<TreeNode> dataList = new ArrayList<>();
        dataList.add(new TreeNode("1", "一级菜单01", "0"));
        dataList.add(new TreeNode("11", "二级菜单01", "1"));
        dataList.add(new TreeNode("1101", "二级菜单0101", "11"));
        dataList.add(new TreeNode("1102", "二级菜单0102", "11"));
        dataList.add(new TreeNode("12", "二级菜单02", "1"));
        dataList.add(new TreeNode("1201", "二级菜单0201", "12"));
        dataList.add(new TreeNode("1202", "二级菜单0202", "12"));
        dataList.add(new TreeNode("13", "二级菜单03", "1"));
        dataList.add(new TreeNode("1301", "二级菜单0301", "13"));

        dataList.add(new TreeNode("2", "一级菜单02", "0"));
        dataList.add(new TreeNode("21", "一级菜单0201", "2"));
        TreeConvert<TreeNode> treeConvert = new TreeConvert<>();

        TimeInterval timer = DateUtil.timer();
        System.out.println(JSON.toJSONString(treeConvert.tree(dataList)));
        System.out.println(timer.interval());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class TreeNode {

        @TreeId
        private String id;

        private String name;

        @TreeFid
        private String paId;
    }
}
