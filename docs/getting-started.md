# workbaseAI-server 本地启动指南

## 检查运行环境

项目使用 JDK 21 和 Spring Boot 4.1.2，需要安装 Maven 3.6.3 或更高版本。[Spring Boot 系统要求](https://docs.spring.io/spring-boot/system-requirements.html)

在包含 `pom.xml` 的项目根目录打开终端，执行以下命令。确认 `java -version` 和 `mvn -v` 显示的 Java 版本都是 21。

```bash
java -version
mvn -v
```

## 下载依赖

首次运行时保持网络连接。在项目根目录执行以下命令，预先下载项目依赖和构建插件。[Maven 依赖插件文档](https://maven.apache.org/plugins/maven-dependency-plugin/go-offline-mojo.html)

```bash
mvn dependency:go-offline
```

看到 `BUILD SUCCESS` 后即可继续启动。

## 按环境启动服务

先填写对应环境 YAML 中的 MySQL 配置，再在项目根目录执行启动命令。未显式指定 Profile 时，`mvn spring-boot:run` 默认使用 `dev`。[Spring Boot Maven 启动文档](https://docs.spring.io/spring-boot/maven-plugin/run.html#run-parameters-details-profiles)

| 环境 | Profile | 端口 | 启动命令 |
| --- | --- | ---: | --- |
| 本地开发 | `dev` | 8080 | `mvn spring-boot:run -Dspring-boot.run.profiles=dev` |
| 测试 | `test` | 8081 | `mvn spring-boot:run -Dspring-boot.run.profiles=test` |
| 预发 | `staging` | 8082 | `mvn spring-boot:run -Dspring-boot.run.profiles=staging` |
| 生产 | `prod` | 8083 | `mvn spring-boot:run -Dspring-boot.run.profiles=prod` |

部署已打包的 JAR 时，在部署环境中设置 `SPRING_PROFILES_ACTIVE`。例如，生产环境设为 `prod`。[Spring Boot Profile 文档](https://docs.spring.io/spring-boot/reference/features/profiles.html)

应用就绪后，终端会显示当前环境。例如，本地开发环境会输出以下内容。

```text
workbaseAI-server 启动成功，当前环境：dev
```

在运行服务的终端按 `Ctrl+C` 停止服务。

## 接口文档与调试

`dev` 和 `test` 环境启用 Knife4j。开发环境地址为 <http://localhost:8080/workbase/doc.html>，测试环境地址为 <http://localhost:8081/workbase/doc.html>。选择接口后可填写参数并发送请求，用户写入和删除操作会作用于当前环境的数据库。

Springdoc 在 `/workbase/v3/api-docs` 提供 OpenAPI JSON，Knife4j 负责展示和调试。预发和生产环境默认关闭文档页面和 JSON 端点。 [Knife4j 版本说明](https://doc.xiaominfo.com/docs/quick-start/start-knife4j-version)

项目自有接口各自使用独立的固定路径，同一模块可以共用路径前缀。参数通过查询字符串或请求体传递。

## 直连 MySQL

应用通过 Spring JDBC 和 HikariCP 连接 MySQL。连接参数写在当前环境对应的 `application-<环境>.yaml` 的 `mysql` 配置块，`spring.datasource` 使用这些参数生成连接地址。当前 Connector/J 版本支持 MySQL Server 8.4 及以上。 [MySQL Connector/J 兼容性说明](https://dev.mysql.com/doc/connector-j/en/connector-j-versions.html)

将 `mysql.host` 从占位地址替换为 MySQL 实例自身可达的域名或 IP。使用阿里云 RDS 时，填主实例的内网连接地址。 [阿里云 RDS 主实例直连说明](https://help.aliyun.com/zh/rds/apsaradb-rds-for-mysql/best-practices-for-rds-duckdb-based-analytical-instances)

| 配置项 | 用途 |
| --- | --- |
| `mysql.host` | MySQL 实例地址 |
| `mysql.port` | MySQL 端口，预填 `3306` |
| `mysql.database` | 已创建的数据库名 |
| `mysql.username` | 数据库账号 |
| `mysql.password` | 数据库密码 |

部署平台可以通过 `MYSQL_PASSWORD` 注入实际密码。

应用就绪前会通过当前数据源执行 `SELECT 1`。检查通过后继续启动，查询失败时终止启动并记录原因。

### 连接池

在对应环境的 YAML 中修改 `spring.datasource.hikari`。时间值以毫秒为单位，连接数按单个应用实例计算。 [HikariCP 配置参考](https://github.com/brettwooldridge/HikariCP/tree/HikariCP-7.0.2#frequently-used)

| 配置项 | 初始值 | 用途 |
| --- | ---: | --- |
| `maximum-pool-size` | 10 | 连接池的总连接数上限 |
| `minimum-idle` | 10 | 连接池尝试保留的最少空闲连接数 |
| `connection-timeout` | 30000 | 从连接池获取连接的最长等待时间 |
| `idle-timeout` | 600000 | 空闲连接回收时间，仅在最少空闲连接数小于总连接数上限时生效 |
| `max-lifetime` | 1800000 | 连接在池中的最长生命周期，正在使用的连接归还后再回收 |

调整连接数时，保持 `minimum-idle` 小于或等于 `maximum-pool-size`。

### MyBatis-Plus

业务 Mapper 放在 `com.workbaseai.server` 的子包，标注 `@Mapper` 并继承 `BaseMapper<实体类型>`。通过构造器注入 Mapper 即可调用 `insert`、`selectById` 等方法。Mapper 与启动健康检查共用 `spring.datasource` 配置的 HikariCP 数据源。 [MyBatis-Plus 快速入门](https://baomidou.com/getting-started/)

### 用户接口

首次使用用户接口前，在目标 MySQL 库中执行 `src/main/resources/db/sys_user.sql`，创建完整的 `sys_user` 表。

| 方法 | 路径 | 用途 |
| --- | --- | --- |
| `GET` | `/workbase/users/detail?id=1` | 按 ID 查询用户 |
| `POST` | `/workbase/users/create` | 新增用户 |
| `PUT` | `/workbase/users/update?id=1` | 修改用户信息 |
| `DELETE` | `/workbase/users/delete?id=1` | 删除用户 |

新增和修改都需要 `username` 和 `displayName`，`email` 和 `status` 可选。新增用户默认启用，`status` 用 `1` 表示启用、`0` 表示停用。修改时省略可选字段会保留原值，传空字符串可清空邮箱。

新增、修改和删除成功时，响应中的 `data` 为布尔值 `true`。查询详情返回用户对象，其中包含由 MySQL 维护的 `createdAt` 和 `updatedAt`。用户名重复时返回 `409`，指定用户不存在时返回 `404`。

```json
{
  "username": "alice",
  "displayName": "小艾",
  "email": "alice@example.com",
  "status": 1
}
```

## 直连 Redis

在对应环境的 YAML 中修改 `spring.data.redis`。将 `host` 和 `port` 填为 Redis 实例的直连地址和端口，`database` 为逻辑库编号。初始值为 `127.0.0.1:6379` 和库 `0`。 [Spring Boot Redis 配置](https://docs.spring.io/spring-boot/reference/data/nosql.html#data.nosql.redis)

实例开启认证时填写 `username` 和 `password`。部署平台可通过 `SPRING_DATA_REDIS_PASSWORD` 注入密码。默认连接超时为 `3s`，命令超时为 `2s`，可在同一配置块内调整。

应用就绪前会向 Redis 执行 `PING`。收到 `PONG` 后继续启动，连接失败或响应异常时终止启动并记录原因。

## 开发时自动重启

用 `mvn spring-boot:run` 启动后，保持服务运行。修改 Java 代码并保存，然后在另一个终端进入项目根目录编译。[Spring Boot DevTools 自动重启文档](https://docs.spring.io/spring-boot/reference/using/devtools.html#using.devtools.restart)

```bash
mvn compile
```

DevTools 检测到编译产物变化后会自动重启应用上下文。终端再次输出带当前环境的启动成功提示后，就可以验证本次改动。使用 IDE 时，也可以通过构建项目触发重启。开启运行期间的自动编译后，保存代码即可触发这一流程。
