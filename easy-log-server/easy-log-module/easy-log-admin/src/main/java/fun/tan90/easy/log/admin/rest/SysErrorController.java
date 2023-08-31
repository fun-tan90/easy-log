package fun.tan90.easy.log.admin.rest;

import cn.hutool.core.util.StrUtil;
import fun.tan90.easy.log.core.convention.Res;
import fun.tan90.easy.log.core.convention.annotation.Log;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/8/29 15:22
 */
@Log
@RestController
public class SysErrorController implements ErrorController {

    @RequestMapping("/error")
    public Res<Void> handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == 404) {
            return Res.errorMsg("资源尚未定义");
        } else {
            return Res.errorMsg(StrUtil.format("{}:服务端发生异常", statusCode));
        }
    }

}
