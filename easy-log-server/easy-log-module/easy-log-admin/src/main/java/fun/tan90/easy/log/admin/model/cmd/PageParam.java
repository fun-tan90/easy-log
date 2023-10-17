package fun.tan90.easy.log.admin.model.cmd;

import lombok.Data;

import java.util.List;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/10/17 17:53
 */
@Data
public class PageParam {

    private Integer pageNum = 1;

    private Integer pageSize = 500;

    private List<String> ascList;

    private List<String> descList;
}