package com.workbaseai.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
class RedisStartupHealthCheck implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(RedisStartupHealthCheck.class);

    private final StringRedisTemplate redisTemplate;

    RedisStartupHealthCheck(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        String response;
        try {
            response = redisTemplate.execute(RedisConnection::ping);
        } catch (DataAccessException exception) {
            throw new IllegalStateException("Redis 启动健康检查失败", exception);
        }

        if (!"PONG".equals(response)) {
            throw new IllegalStateException("Redis 启动健康检查失败，PING 未返回 PONG");
        }

        logger.info("Redis 启动健康检查通过");
    }
}
