package com.workbaseai.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
class MysqlStartupHealthCheck implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(MysqlStartupHealthCheck.class);

    private final JdbcClient jdbcClient;

    MysqlStartupHealthCheck(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            jdbcClient.sql("SELECT 1").query(Integer.class).single();
        } catch (DataAccessException exception) {
            throw new IllegalStateException("MySQL 启动健康检查失败", exception);
        }

        logger.info("MySQL 启动健康检查通过");
    }
}
