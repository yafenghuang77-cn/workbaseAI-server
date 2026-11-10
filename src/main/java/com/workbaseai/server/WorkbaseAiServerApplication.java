package com.workbaseai.server;

import com.alibaba.cola.catchlog.CatchLogAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Import(CatchLogAutoConfiguration.class)
public class WorkbaseAiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkbaseAiServerApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void printStartupSuccess(ApplicationReadyEvent event) {
        Environment environment = event.getApplicationContext().getEnvironment();
        String[] activeProfiles = environment.getActiveProfiles();
        String[] profiles = activeProfiles.length == 0 ? environment.getDefaultProfiles() : activeProfiles;
        System.out.println("workbaseAI-server 启动成功，当前环境：" + String.join(", ", profiles));
    }
}
