# Online Music Player - 在线音乐播放器

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.7.x-brightgreen?logo=springboot" alt="Spring Boot">
  <img src="https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql" alt="MySQL">
  <img src="https://img.shields.io/badge/MyBatis-3.5.x-yellowgreen?logo=apache" alt="MyBatis">
  <img src="https://img.shields.io/badge/Java-8+-orange?logo=java" alt="Java">
  <img src="https://img.shields.io/badge/Maven-3.6+-red?logo=apachemaven" alt="Maven">
</p>

这是一个基于 Spring Boot 和 MyBatis 实现的在线音乐播放器Web应用。项目实现了用户登录、音乐上传播放、歌曲收藏、模糊搜索等核心功能，并采用前后端分离的开发模式。

该项目旨在实践和巩固Web后端开发中的关键技术，特别是**自定义认证授权**、**RESTful API设计**以及**文件服务**的实现。

## ✨ 功能特性 (Features)

*   **用户认证**: 基于`Session`和`HandlerInterceptor`实现的登录拦截。
*   **密码安全**: 使用`BCryptPasswordEncoder`对用户密码进行哈希加盐存储。
*   **音乐管理**:
    *   支持MP3格式音乐的上传与信息入库。
    *   提供音乐文件的二进制流式下载/播放在线。
    *   支持音乐的单曲删除和批量删除。
*   **播放列表**:
    *   根据歌名进行模糊搜索。
    *   展示所有音乐列表。
*   **我的收藏**:
    *   用户可以收藏喜欢的音乐到个人列表。
    *   支持取消收藏和在收藏列表中进行搜索。
*   **统一API**: 所有后端接口均提供统一的JSON响应格式。

## 🚀 技术栈 (Technology Stack)

*   **核心框架**: Spring Boot 2.7.x
*   **持久层**: MyBatis, Druid Connection Pool
*   **数据库**: MySQL 8.0
*   **认证授权**: 自定义`HandlerInterceptor` + `Session`
*   **密码加密**: Spring Security Crypto (`BCryptPasswordEncoder`)
*   **构建工具**: Apache Maven
*   **服务器**: 内嵌 Tomcat

## 📖 快速开始 (Getting Started)

### 1. 环境准备

*   JDK 1.8 或更高版本
*   Maven 3.6 或更高版本
*   MySQL 8.0 或更高版本
*   IDE (如 IntelliJ IDEA)

### 2. 克隆项目

```bash
git clone https://github.com/dujiahuiyi/online-music-player.git
cd online-music-player
```

### 3. 数据库配置

1.  在你的MySQL中创建一个新的数据库，例如 `onlinemusic`。
2.  找到项目根目录下的 `db.sql` 文件，将其导入到你刚创建的数据库中，以初始化表结构和基础数据。
3.  打开 `src/main/resources/application.properties` 文件，修改以下数据库连接信息以匹配你自己的配置：

    ```properties
    # 数据库驱动和URL (请确保你已创建名为 onlinemusic 的数据库)
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.url=jdbc:mysql://localhost:3306/onlinemusic?characterEncoding=utf-8
    # 数据库用户名和密码
    spring.datasource.username=root
    spring.datasource.password=123456
    ```

### 4. 文件存储路径配置

在 `src/main/resources/application.properties` 文件中，配置音乐文件上传后在服务器上存储的本地路径：

```properties
# 音乐文件在服务器上的存储目录
music.local.path=D:/music-upload/
```
请确保该目录存在且应用有权限读写。

### 5. 运行项目

你可以通过以下两种方式运行此项目：

*   **通过IDE**: 直接在你的IDE中找到 `MyMusicApplication.java` 文件，右键点击并选择 "Run" 或 "Debug"。
*   **通过Maven命令行**:
    ```bash
    mvn spring-boot:run
    ```

项目启动成功后，默认会运行在 `8080` 端口。在浏览器中访问 `http://localhost:8080/login.html` 即可看到登录页面。

## 💡 核心设计与实现

### 自定义认证授权
本项目没有直接引入Spring Security的复杂配置，而是通过实现`HandlerInterceptor`接口，手动构建了一套轻量级的认证系统。
*   **`LoginInterceptor`**: 拦截所有需要登录才能访问的API。它通过检查`HttpSession`中是否存在用户信息来判断用户是否已登录。如果未登录，则返回`401 Unauthorized`状态码，遵循了RESTful的最佳实践。
*   **`WebMvcConfigurer`**: 在`AppConfig`中配置拦截器，并精确地定义了需要拦截和放行的URL模式，如放行登录接口、静态资源等。

### 统一API响应与异常处理
为了提升前后端协作效率和代码健壮性，项目采用了统一的API设计规范。
*   **`ResponseBodyMessage<T>`**: 这是一个泛型类，用于封装所有API的响应数据。它包含了`status`（业务状态码）、`message`（提示信息）和`data`（实际数据），使得前端可以编写统一的逻辑来处理后端响应。
*   **自定义业务异常**: 通过创建如`AlreadyLikedException`等自定义`RuntimeException`，在Service层抛出。Controller层通过`try-catch`捕获这些异常，并将其转换为对用户友好的`ResponseBodyMessage`，避免了在Controller中出现大量`if-else`判断，使代码更清晰。

### 文件服务与数据一致性
*   **上传与回滚**: `MusicService`中的文件上传逻辑是一个小型事务。当物理文件成功上传到服务器，但数据库插入操作失败时，程序会**自动删除已上传的文件**，防止产生脏数据，保证了文件系统和数据库状态的一致性。
*   **级联删除**: 当管理员删除一首音乐时，系统不仅会删除`music`表中的记录和服务器上的物理文件，还会同步删除`lovemusic`（收藏）表中所有与该音乐相关的记录，保证了数据的引用完整性。
