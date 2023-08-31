package fun.tan90.easy.log.admin.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/19 8:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserInfoVo {

    private String userId;

    private String userName;

    private List<String> roles;

    private List<String> permissions;
}
