package com.chj.easy.log.core.convention.exception;


import com.chj.easy.log.core.convention.enums.IErrorCode;

/**
 * description 客户端异常
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/3/24 11:55
 */
public class ClientException extends AbstractException {

    public ClientException(IErrorCode errorCode) {
        this(null, null, errorCode);
    }

    public ClientException(String message) {
        this(message, null, null);
    }

    public ClientException(String message, IErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public ClientException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable, errorCode);
    }

    @Override
    public String toString() {
        return "ClientException{" +
                "code='" + errorCode + "'," +
                "message='" + errorMessage + "'" +
                '}';
    }
}
