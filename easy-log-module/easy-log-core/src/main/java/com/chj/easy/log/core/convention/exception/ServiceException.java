package com.chj.easy.log.core.convention.exception;


import com.chj.easy.log.core.convention.enums.IErrorCode;

/**
 * description 服务端异常
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/3/24 11:55
 */
public class ServiceException extends AbstractException {

    public ServiceException(String message) {
        this(message, null, null);
    }

    public ServiceException(IErrorCode errorCode) {
        this(null, errorCode);
    }

    public ServiceException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public ServiceException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "ServiceException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
