package com.allen.sys.config;

import com.allen.sys.annotation.NotControllerResponseAdvice;
import com.allen.sys.common.ResponseBean;
import com.allen.sys.common.ResponseBeanUtil;
import com.allen.sys.constants.ResponseCodeEnum;
import com.allen.sys.exception.APIException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一包装响应
 *
 * https://mp.weixin.qq.com/s/594-WxyNCi2rDgp8hJTdsw
 *
 * @author xuguocai
 * @date 2022/8/5 14:15
 */
@RestControllerAdvice(basePackages = {"com.allen.sys.controller"})
public class ControllerResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        return !(returnType.getParameterType().isAssignableFrom(ResponseBean.class)
                || returnType.hasMethodAnnotation(NotControllerResponseAdvice.class));
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // String 类型不能直接包装
        if (returnType.getGenericParameterType().equals(String.class)){
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                return objectMapper.writeValueAsString(new ResponseBean(body));
            } catch (JsonProcessingException e) {
                throw new APIException(ResponseCodeEnum.FAIL,e.getMessage());
            }
        }
        return ResponseBeanUtil.ok(body);
    }

    @ExceptionHandler({BindException.class})
    public ResponseBean MethodArgumentNotValidExceptionHandler(BindException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        return new ResponseBean(ResponseCodeEnum.FAIL, objectError.getDefaultMessage());
    }

    @ExceptionHandler(APIException.class)
    public ResponseBean APIExceptionHandler(APIException e) {
        // log.error(e.getMessage(), e); 由于还没集成日志框架，暂且放着，写上TODO
        return new ResponseBean(e.getCode(), e.getMsg(), e.getMessage());
    }
}
