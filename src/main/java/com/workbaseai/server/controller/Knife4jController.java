package com.workbaseai.server.controller;

import java.util.Map;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Hidden
@RestController
class Knife4jController {

    private static final String API_DOCS_PATH = "/v3/api-docs";

    private final boolean apiDocsEnabled;

    Knife4jController(@Value("${springdoc.api-docs.enabled:false}") boolean apiDocsEnabled) {
        this.apiDocsEnabled = apiDocsEnabled;
    }

    @GetMapping(value = "/doc.html", produces = MediaType.TEXT_HTML_VALUE)
    Resource documentation() {
        requireApiDocsEnabled();
        return new ClassPathResource("META-INF/resources/doc.html");
    }

    @GetMapping(value = API_DOCS_PATH + "/swagger-config", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, String> configuration(HttpServletRequest request) {
        requireApiDocsEnabled();
        return Map.of("url", request.getContextPath() + API_DOCS_PATH);
    }

    private void requireApiDocsEnabled() {
        if (!apiDocsEnabled) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
