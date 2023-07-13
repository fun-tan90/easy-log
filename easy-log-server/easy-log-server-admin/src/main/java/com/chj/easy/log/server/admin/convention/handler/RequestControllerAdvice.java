package com.chj.easy.log.server.admin.convention.handler;


import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.chj.easy.log.server.admin.convention.annotation.ReqBodyDecrypt;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * description
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/1/31 14:06
 */
@Component
@RestControllerAdvice(annotations = RestController.class)
public class RequestControllerAdvice implements RequestBodyAdvice {

    private static final String AES_KEY = "key1234567890key";

    private static final AES AES = SecureUtil.aes(AES_KEY.getBytes());

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (methodParameter.hasMethodAnnotation(ReqBodyDecrypt.class)) {
            return true;
        }
        return methodParameter.getDeclaringClass().getAnnotation(ReqBodyDecrypt.class) != null;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        int available = inputMessage.getBody().available();
        if (available <= 0) {
            return inputMessage;
        }
        return new HttpInputMessage() {
            @Override
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream(AES.decrypt(IoUtil.readBytes(inputMessage.getBody())));
            }
        };
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return null;
    }
}
