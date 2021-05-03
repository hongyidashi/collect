package org.collect.registry.common.aop;

import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import org.collect.registry.common.api.ResponseVo;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述: 全局处理响应数据
 *
 * @author: panhongtong
 * @date: 2021-03-03 22:12
 **/
@RestControllerAdvice(basePackages = {"org.collect.registry"})
public class ResponseControllerAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 判断哪些是需要拦截的，放过本身已经是ResponseVo的结果
     *
     * @param returnType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.getParameterType().equals(ResponseVo.class);
    }

    /**
     * 对响应结果进行统一封装成ResponseVo
     *
     * @param body
     * @param returnType
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     * @return
     */
    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // String类型不能直接包装，所以要进行些特别的处理
        if (returnType.getGenericParameterType().equals(String.class)) {
            Map<String, Object> map = new HashMap<>(1);
            map.put("value", body);
            String str = JSONUtil.toJsonStr(ResponseVo.data(body));
            response.getBody().write(str.getBytes(StandardCharsets.UTF_8));
            return null;
        }
        return ResponseVo.data(body);
    }
}
