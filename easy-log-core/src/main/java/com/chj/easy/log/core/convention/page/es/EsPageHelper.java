package com.chj.easy.log.core.convention.page.es;

import com.chj.easy.log.core.model.Doc;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * description 分页工具
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/14 22:07
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EsPageHelper {

    /**
     * 获取分页信息
     *
     * @param list     数据列表
     * @param total    总数
     * @param pageNum  当前页
     * @param pageSize 总页数
     * @param <T>      数据类型
     * @return 分页信息
     */
    public static EsPageInfo<Doc> getPageInfo(List<Doc> list, Long total, Integer pageNum, Integer pageSize) {
        EsPageInfo<Doc> pageInfo = new EsPageInfo<>(list);
        pageInfo.setTotal(total);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setPages((int) (total % pageSize > 0 ? total / pageSize + 1 : total / pageSize));
        // 计算导航页
        pageInfo.calcNavigatePageNums();
        // 计算前后页,第一页,最后一页
        pageInfo.calcPage();
        // 判断页面边界
        pageInfo.judgePageBoundary();
        return pageInfo;
    }
}
