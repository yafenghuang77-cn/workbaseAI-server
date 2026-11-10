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

## 启动和停止服务

在项目根目录执行以下命令。Maven 会编译项目并启动服务，也会按需下载缺少的依赖。[Spring Boot Maven 启动文档](https://docs.spring.io/spring-boot/maven-plugin/run.html)

```bash
mvn spring-boot:run
```

应用就绪后，终端会显示以下提示。

```text
workbaseAI-server 启动成功
```

在运行服务的终端按 `Ctrl+C` 停止服务。
