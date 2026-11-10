package com.workbaseai.server.config;

import java.util.Map;
import java.util.Set;

import com.alibaba.cola.catchlog.ApplicationContextHelper;
import com.alibaba.cola.catchlog.DefaultResponseHandler;
import com.alibaba.cola.catchlog.ResponseHandlerI;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webmvc.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

@Configuration(proxyBeanMethods = false)
@Import(ApplicationContextHelper.class)
class ApiErrorConfiguration {

    private static final String DEFAULT_ERROR_CODE = "9000";
    private static final String DEFAULT_ERROR_MESSAGE = "请求处理失败";
    private static final Set<String> COLA_DEFAULT_CODES = Set.of("BIZ_ERROR", "SYS_ERROR", "UNKNOWN_ERROR");

    @Bean
    ResponseHandlerI colaResponseHandler() {
        DefaultResponseHandler handler = new DefaultResponseHandler();
        return (returnType, code, message) -> {
            boolean hasCustomCode = StringUtils.hasText(code) && !COLA_DEFAULT_CODES.contains(code);
            String errorCode = hasCustomCode ? code : DEFAULT_ERROR_CODE;
            String errorMessage = StringUtils.hasText(message) ? message : DEFAULT_ERROR_MESSAGE;
            return handler.handle(returnType, errorCode, errorMessage);
        };
    }

    @Bean
    DefaultErrorAttributes apiErrorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest request, ErrorAttributeOptions options) {
                Map<String, Object> attributes = super.getErrorAttributes(request, options);
                attributes.put("success", false);
                attributes.put("errCode", DEFAULT_ERROR_CODE);
                attributes.put("errMessage", attributes.getOrDefault("error", DEFAULT_ERROR_MESSAGE));
                attributes.put("data", null);
                return attributes;
            }
        };
    }
}
