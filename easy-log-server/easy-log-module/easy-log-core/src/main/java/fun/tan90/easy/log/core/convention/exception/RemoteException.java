package fun.tan90.easy.log.core.convention.exception;


import fun.tan90.easy.log.core.convention.enums.IErrorCode;

/**
 * description 远程服务调用异常
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/3/24 11:55
 */
public class RemoteException extends AbstractException {

    public RemoteException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public RemoteException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "RemoteException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
