# workbaseAI-server

基于 Java 21 和 Spring Boot 4.1.2 的后端服务，通过 MyBatis-Plus 访问 MySQL，并使用 Redis。

## 1. 下载依赖

安装 JDK 21 和 Maven，确认 `mvn -v` 显示的 Java 版本为 21。在项目根目录执行以下命令，预先下载依赖和构建插件。

```bash
mvn dependency:go-offline
```

## 2. 启动项目

在 [application-dev.yaml](src/main/resources/application-dev.yaml) 中填写 `mysql` 和 `spring.data.redis` 的连接配置。应用启动时会检查 MySQL 和 Redis 是否可用。首次调用用户接口前，在配置的 MySQL 库中执行 [sys_user.sql](src/main/resources/db/sys_user.sql) 建表。

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## 3. 访问 Knife4j

应用启动成功后，在浏览器打开对应环境的 Knife4j 页面。选择接口、填写参数，即可发送请求并查看响应。

| 环境 | Knife4j | OpenAPI JSON |
| --- | --- | --- |
| 开发 `dev` | <http://localhost:8080/workbase/doc.html> | <http://localhost:8080/workbase/v3/api-docs> |
| 测试 `test` | <http://localhost:8081/workbase/doc.html> | <http://localhost:8081/workbase/v3/api-docs> |

Knife4j 在 `dev` 和 `test` 环境开启。其他环境的启动命令和接口说明见 [本地启动指南](docs/getting-started.md)。
