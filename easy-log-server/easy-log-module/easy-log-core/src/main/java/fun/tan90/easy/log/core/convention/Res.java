package fun.tan90.easy.log.core.convention;

import fun.tan90.easy.log.core.convention.enums.IErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * description
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/1/31 13:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Res<T> implements Serializable {

    private static final long serialVersionUID = -3243475556181265119L;

    private String code;

    private String msg;

    private T data;

    public static Res<Void> ok() {
        return new Res<>(IErrorCode.OK.getCode(), IErrorCode.OK.getMessage(), null);
    }

    public static <T> Res<T> ok(T data) {
        return new Res<>(IErrorCode.OK.getCode(), IErrorCode.OK.getMessage(), data);
    }

    public static <T> Res<T> ok(String msg, T data) {
        return new Res<>(IErrorCode.OK.getCode(), msg, data);
    }

    public static Res<Void> error() {
        return new Res<>(IErrorCode.SYS_2000001.getCode(), IErrorCode.SYS_2000001.getMessage(), null);
    }

    public static <T> Res<T> error(T data) {
        return new Res<>(IErrorCode.SYS_2000001.getCode(), IErrorCode.SYS_2000001.getMessage(), data);
    }

    public static Res<Void> error(IErrorCode iErrorCode) {
        return new Res<>(iErrorCode.getCode(), iErrorCode.getMessage(), null);
    }

    public static <T> Res<T> error(IErrorCode iErrorCode, T data) {
        return new Res<>(iErrorCode.getCode(), iErrorCode.getMessage(), data);
    }

    public static Res<Void> errorMsg(String msg) {
        return new Res<>(IErrorCode.SYS_2000001.getCode(), msg, null);
    }

    public static Res<Void> errorCodeAndMsg(String code, String msg) {
        return new Res<>(code, msg, null);
    }

    public static <T> Res<T> errorMsgAndData(String msg, T data) {
        return new Res<>(IErrorCode.SYS_2000001.getCode(), msg, data);
    }
}
