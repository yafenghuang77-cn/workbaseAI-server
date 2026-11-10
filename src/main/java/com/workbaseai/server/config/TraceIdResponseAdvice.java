package com.workbaseai.server.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.alibaba.cola.dto.Response;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.boot.webmvc.autoconfigure.error.BasicErrorController;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
class TraceIdResponseAdvice implements ResponseBodyAdvice<Object> {

    private static final String TRACE_ID_ATTRIBUTE = TraceIdResponseAdvice.class.getName() + ".traceId";

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return JacksonJsonHttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType contentType,
            Class<? extends HttpMessageConverter<?>> converterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        if (body instanceof Response colaResponse) {
            return new ResponseWithTraceId(colaResponse, traceId(request));
        }
        if (body instanceof Map<?, ?> attributes
                && BasicErrorController.class.isAssignableFrom(returnType.getContainingClass())) {
            Map<Object, Object> errorBody = new LinkedHashMap<>(attributes);
            errorBody.put("traceId", traceId(request));
            return errorBody;
        }
        return body;
    }

    private static String traceId(ServerHttpRequest request) {
        return (String) request.getAttributes().computeIfAbsent(TRACE_ID_ATTRIBUTE,
                key -> UUID.randomUUID().toString().replace("-", ""));
    }

    private record ResponseWithTraceId(@JsonUnwrapped Response body, String traceId) {
    }
}
