package com.chj.easy.log.server.common.convention.handler;

import com.chj.easy.log.server.common.convention.Res;
import com.chj.easy.log.server.common.convention.annotation.NoResult;
import com.chj.easy.log.server.common.convention.enums.IErrorCode;
import com.chj.easy.log.server.common.convention.exception.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * description
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2022/8/17 8:31
 */
@Slf4j
@Component
@RestControllerAdvice(annotations = RestController.class)
public class ResponseControllerAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
        if (returnType.hasMethodAnnotation(NoResult.class)) {
            return false;
        }
        if (returnType.getDeclaringClass().getAnnotation(NoResult.class) != null) {
            return false;
        }
        // 如果接口返回的类型本身就是Result那就没有必要进行额外的操作，返回false
        Class<?> parameterType = returnType.getParameterType();
        if (parameterType.equals(ResponseEntity.class)) {
            return false;
        }
        return !parameterType.equals(Res.class);
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        if (returnType.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 将数据包装在ResultVO里后，再转换为json字符串响应给前端
                return objectMapper.writeValueAsString(Res.ok(data));
            } catch (JsonProcessingException e) {
                throw new ServiceException(IErrorCode.SYS_2000001);
            }
        }
        return Res.ok(data);
    }
}