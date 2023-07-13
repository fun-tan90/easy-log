package com.chj.easy.log.server.common.convention.exception;

import com.chj.easy.log.server.common.convention.enums.IErrorCode;
import lombok.Getter;


/**
 * description 抽象项目中三类异常体系，客户端异常、服务端异常以及远程服务调用异常
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/6/8 23:10
 */
@Getter
public abstract class AbstractException extends RuntimeException {

    public final String errorCode;

    public final String errorMessage;

    public AbstractException(String message, Throwable throwable, IErrorCode errorCode) {
        super(message, throwable);
        errorCode = null == errorCode ? IErrorCode.SYS_2000001 : errorCode;
        this.errorCode = errorCode.getCode();
        this.errorMessage = message == null ? errorCode.getMessage() : message;
    }
}
