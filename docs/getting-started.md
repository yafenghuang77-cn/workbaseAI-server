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

在项目根目录执行对应环境的命令。未显式指定 Profile 时，`mvn spring-boot:run` 默认使用 `dev`。[Spring Boot Maven 启动文档](https://docs.spring.io/spring-boot/maven-plugin/run.html#run-parameters-details-profiles)

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

## 开发时自动重启

用 `mvn spring-boot:run` 启动后，保持服务运行。修改 Java 代码并保存，然后在另一个终端进入项目根目录编译。[Spring Boot DevTools 自动重启文档](https://docs.spring.io/spring-boot/reference/using/devtools.html#using.devtools.restart)

```bash
mvn compile
```

DevTools 检测到编译产物变化后会自动重启应用上下文。终端再次输出带当前环境的启动成功提示后，就可以验证本次改动。使用 IDE 时，也可以通过构建项目触发重启。开启运行期间的自动编译后，保存代码即可触发这一流程。
