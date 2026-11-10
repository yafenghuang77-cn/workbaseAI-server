package com.workbaseai.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class WorkbaseAiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkbaseAiServerApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void printStartupSuccess() {
        System.out.println("workbaseAI-server 启动成功");
    }
}
