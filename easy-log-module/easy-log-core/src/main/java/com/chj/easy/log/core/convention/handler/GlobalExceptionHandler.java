package com.chj.easy.log.core.convention.handler;

import com.chj.easy.log.core.convention.Res;
import com.chj.easy.log.core.convention.exception.AbstractException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2022/8/17 8:31
 */
@Slf4j
@Component
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Res<Void> onException(MaxUploadSizeExceededException e) {
        return Res.errorMsg("上传文件过大");
    }

    @ExceptionHandler(BindException.class)
    public Res<String> onException(BindException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String message = fieldErrors.stream().map(n -> n.getField() + ":" + n.getDefaultMessage()).collect(Collectors.joining(";"));
        log.error("==BindException=={}", message);
        return Res.errorMsgAndData("参数绑定异常", message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Res<String> onException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String message = fieldErrors.stream().map(n -> n.getField() + ":" + n.getDefaultMessage()).collect(Collectors.joining(";"));
        log.error("==MethodArgumentNotValidException=={}", message);
        return Res.errorMsgAndData("入参异常", message);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Res<Void> onException(HttpRequestMethodNotSupportedException e) {
        log.error("==HttpRequestMethodNotSupportedException=={}", e.getMessage());
        return Res.errorMsg("请求方法【" + e.getMethod() + "】类型错误");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Res<Void> onException(HttpMediaTypeNotSupportedException e) {
        log.error("==HttpMediaTypeNotSupportedException=={}", e.getMessage());
        return Res.errorMsg("MediaType类型【" + e.getContentType() + "】类型错误");
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Res<String> onException(SQLIntegrityConstraintViolationException e) {
        String message = e.getMessage();
        log.error("==SQLIntegrityConstraintViolationException=={}", message);
        return Res.errorMsgAndData("Duplicate Key", message);
    }

    @ExceptionHandler(NullPointerException.class)
    public Res<String> onNullPointerException(NullPointerException e) {
        e.printStackTrace();
        String message = e.getMessage();
        log.error("==NullPointerException=={}==class=={}", message, e.getClass());
        return Res.errorMsgAndData("系统发生空指针异常", message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Res<String> onIllegalArgumentException(IllegalArgumentException e) {
        e.printStackTrace();
        String message = e.getMessage();
        log.error("==IllegalArgumentException=={}==class=={}", message, e.getClass());
        return Res.errorMsgAndData("参数校验异常", message);
    }

    @ExceptionHandler(AbstractException.class)
    public Res<Void> abstractException(HttpServletRequest request, AbstractException ex) {
        if (ex.getCause() != null) {
            log.error("[{}] {} [ex] {}", request.getMethod(), request.getRequestURL().toString(), ex, ex.getCause());
            return Res.errorCodeAndMsg(ex.getErrorCode(), ex.getErrorMessage());
        }
        log.error("[{}] {} [ex] {}", request.getMethod(), request.getRequestURL().toString(), ex.toString());
        return Res.errorCodeAndMsg(ex.getErrorCode(), ex.getErrorMessage());
    }

    @ExceptionHandler(Throwable.class)
    public Res<Void> defaultErrorHandler(HttpServletRequest request, Throwable throwable) {
        log.error("[{}] {} ", request.getMethod(), getUrl(request), throwable);
        return Res.errorMsg(throwable.getMessage());
    }

    private String getUrl(HttpServletRequest request) {
        if (!StringUtils.hasLength(request.getQueryString())) {
            return request.getRequestURL().toString();
        }
        return request.getRequestURL().toString() + "?" + request.getQueryString();
    }
}
