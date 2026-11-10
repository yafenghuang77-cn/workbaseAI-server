package com.workbaseai.server.controller;

import java.util.Map;

import com.alibaba.cola.dto.SingleResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public SingleResponse<Map<String, String>> health() {
        return SingleResponse.of(Map.of("status", "UP"));
    }
}
