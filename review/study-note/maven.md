# Maven 笔记

## Maven 简介

传统项目管理状态分析：
- jar 包不统一，jar 包不兼容
- 工程升级维护过程操作繁琐
- ...

### Maven 是什么(what)

- Maven 的本质是一个项目管理工具，将项目开发和管理过程抽象成一个项目对象模型 (POM)
- POM (Project Object Model): 项目对象模型

![](http://assets.processon.com/chart_image/62731f39e401fd1b246be716.png)

Maven 的作用：
1. 项目构建：提供标准的、跨平台的自动化项目构建方式
2. 依赖管理：方便快捷的管理项目依赖的资源(jar包)，避免资源间的版本冲突问题
3. 统一开发结构：提供标准的、统一的项目结构

项目结构：
- src
    - main
        - java
        - resources
    - test
        - java
        - resources

## Maven 的基础概念

### 仓库

- 仓库：用于存资源，包含各种 jar 包
- 仓库分类
    - 本地仓库：自己电脑上存储资源的仓库，连接远程仓库获取资源
    - 远程仓库：非本机电脑上的仓库，为本地仓库提供资源
        - 中央仓库：由 maven 开发团队管理，存储所有资源的仓库
        - 私服：部门、公司范围内存储资源的仓库，从中央仓库获取资源。本地访问私服，由私服从中央仓库获取资源文件
- 私服的作用
    - 保护具有版权的资源，包含购买的或自主研发的 jar，中央仓库中的 jar 都是开源的不能存储具有版权的资源
    - 一定范围内共享资源，仅对内部开放，不对外共享
    - 加快本地仓库获取资源的速度

### 坐标

Maven 中的坐标用于描述仓库中资源的位置。https://repo1.maven.org/maven2/ 。

Maven 坐标主要组成：
- groupId: 定义当前 Maven 项目隶属组织名称(通常是域名反写，例如 org.mybatis)
- artifactId: 定义当前 Maven 项目名称(通常是模块名称，例如 CRM、SMS)
- version: 定义当前项目版本号
- packaging: 定义该项目的打包方式

Maven 坐标的作用：使用唯一标识，唯一性定位资源位置，通过该标识可以将资源的标识与下载工作交给机器完成

### maven 配置

maven 的配置在 *settings.xml* 中。

1. 配置本地仓库路径：`<localRepository>/path/to/local/repo</localRepository>`
2. 配置远程仓库地址(配置中央仓库为镜像仓库) 
```xml
<mirrors>
    <mirror>
        <!-- 给镜像起的名称 -->
        <id>alimaven</id>
        <!-- 名称，并不重要 -->
        <name>aliyun maven</name>
        <!-- 镜像地址 -->
        <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
        <!-- 表示被镜像对象，添 central 表示镜像中央仓库 -->
        <mirrorOf>central</mirrorOf>
    </mirror>
</mirrors>
```

## 第一个 maven 项目


### 手工创建工程

Maven 过程目录结构：
```
project
└── java-project
    └── src
        ├── main : 写程序
        │   ├── java : java 源程序
        │   └── resources : 配置文件
        └── test : 写测试程序
            ├── java : 测试源程序
            └── resources : 测试的配置文件
```
Maven 项目构建命令：
- `mvn compile`: 编译项目
- `mvn clean`: 清理项目
- `mvn test`: 项目测试
- `mvn package`: 项目打包
- `mvn install`: 将打包的jar包放到本地仓库中

### 插件创建工程

```
mvn archetype:generate
    -DgroupId={project-packing}
    -DartifactId={project-name}
    -DarchetypeArtifactId=maven-archetype-quickstart
    -DinteractiveMode=false 
```
创建 java 工程的范例：
```
mvn archetype:generate -DgroupId=com.bfh -DartifactId=myjava -DarchetypeArtifactId=maven-archetype-quickstart -Dversion=0.0.1-snapshot -DinteractiveMode=false
```
创建 web 工程范例：
```
mvn archetype:generate -DgroupId=com.bfh -DartifactId=myweb -DarchetypeArtifactId=maven-archetype-webapp -Dversion=0.0.1-snapshot -DinteractiveMode=false
```

创建的 java 工程的目录结构为：
```
myjava
├── pom.xml
└── src
    ├── main
    │   └── java
    │       └── com
    │           └── bfh
    │               └── App.java
    └── test
        └── java
            └── com
                └── bfh
                    └── AppTest.java
```
其中 *pom.xml* 中有一句：`<packaging>jar</packaging>`，表示打的是 jar 包。

创建的 web 工程的目录结构为：
```
myweb
├── pom.xml
└── src
    └── main
        ├── resources
        └── webapp
            ├── WEB-INF
            │   └── web.xml
            └── index.jsp
```
其中 *pom.xml* 中有一句：`<packaging>war</packaging>`，表示打的是 war 包。

## pom 文件分析

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project 
    xmlns="http://maven.apache.org/POM/4.0.0" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">

  <modelVersion>4.0.0</modelVersion>
  <!-- 打包方式, web 工程打包为 war，java 工程打包为 jar -->
  <packaging>war</packaging>

  <!-- 名称不重要 -->
  <name>java03</name>
  <!-- 组织 id -->
  <groupId>com.bfh</groupId>
  <!-- 项目 id -->
  <artifactId>java03</artifactId>
  <!-- 项目版本号 -->
  <version>1.0-SNAPSHOT</version>

  <!-- 当前工程的所有依赖 -->
  <dependencies>
    <!-- 具体的依赖 -->
    <dependency>
      <groupId>com.bfh</groupId>
      <artifactId>[the artifact id of the block to be mounted]</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
  </dependencies>

  <build>
    <!-- 构建的插件配置 -->
    <plugins>
      <plugin>
        <!-- https://mvnrepository.com/artifact/org.apache.tomcat.maven/tomcat7-maven-plugin -->
          <groupId>org.apache.tomcat.maven</groupId>
          <artifactId>tomcat7-maven-plugin</artifactId>
          <version>2.1</version>
          <configuration>
            <port>8080</port>
            <path>/</path>
          </configuration>

      </plugin>
    </plugins>
  </build>

</project>
```

## 依赖管理

### 依赖配置

依赖指定当前项目所需要的 jar，一个项目可以设置多个依赖。
格式：
```xml
<!-- 设置当前项目所依赖的所有 jar -->
<dependencies>
    <!-- 设置具体的依赖 -->
    <dependency>
        <!-- 依赖所属群组id -->
        <groupId>junit</groupId>
        <!-- 依赖所属项目id -->
        <artifactId>junit</artifactId>
        <!-- 依赖版本号 -->
        <version>4.12</version>
    </dependency>
</dependencies>
```

### 依赖传递

- 依赖具有传递性
    - 直接依赖：在当前项目中通过依赖配置建立的依赖关系
    - 间接依赖：被依赖的资源如果依赖其他资源，当前项目间接依赖其他资源

- 依赖传递冲突问题
    - 路径优先，当依赖中出现相同的资源时，层级越深，优先级越低，层级越浅，优先级越高
    - 声明优先：当资源在相同层级被依赖时，配置顺序靠前的覆盖配置顺序靠后的
    - 特殊优先：当同级配置了相同资源的不同版本，后配置的覆盖先配置的

- 可选依赖：可选依赖指**对外隐藏**当前所依赖的资源 -- **不透明**
```xml
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <optional>true</optional>
</dependency>
```

- 排除依赖：指主动断开依赖的资源，被排除的资源无序指定版本，表示**不需要**，使用 `excludes`
```xml
    <dependency>
      <groupId>com.bfh</groupId>
      <artifactId>java01</artifactId>
      <version>1.0-SNAPSHOT</version>
      <exclusions>
        <!-- 排除 com.bfh:fjar -->
        <exclusion>
          <groupId>com.bfh</groupId>
          <artifactId>fjar</artifactId>
        </exclusion>
      </exclusion
```

### 依赖范围

依赖的 jar 默认情况可以在任何地方使用，可以通过 scope 标签设定其作用范围：

- 作用范围：
    - 主程序范围有效 (main 文件范围内)
    - 测试程序范围有效 (test 文件范围内)
    - 是否参与打包 (package 指令范围内)

|scope|主代码|测试代码|打包|范例|
|---|---|---|---|---|
|compile(默认)|Y|Y|Y|log4j|
|test|N|Y|N|junit|
|provided|Y|Y|N|servlet-api|
|runtime|N|N|Y|jdbc|

依赖范围传递性（带有依赖范围的资源在进行传递时，作用范围将受到影响）：


||compile|test|provided|runtime|
|---|---|---|---|---|
|compile|compile|test|provided|runtime|
|test|
|provided|
|runtime|runtime|test|provided|runtime|

## 生命周期与插件

项目构建生命周期。

Maven 对项目构建的生命周期划分为3套：
- clean：清理工作
    - pre-clean：执行一些需要在 clean 之前完成的工作
    - clean：移除上一次构建生成的文件
    - post-clean：执行一些需要在 clean 之后立刻完成的工作
- default：核心工作，如编译、测试、打包、部署等
    - validate(校验): 校验项目是否正确并且所有必要的信息可以完成项目的构建过程。
    - initialize(初始化): 初始化构建状态，比如设置属性值。
    - generate-sources(生成源代码): 生成包含在编译阶段中的任何源代码。
    - process-sources(处理源代码): 处理源代码，比如说，过滤任意值。
    - generate-resources(生成资源文件): 生成将会包含在项目包中的资源文件。
    - process-resources(处理资源文件): 复制和处理资源到目标目录，为打包阶段做好准备。
    - **compile**(编译): 编译项目的源代码。
    - process-classes(处理类文件): 处理编译生成的文件，比如说对 Java class文件做字节码改善优化。
    - generate-test-sources(生成测试源代码): 生成包含在编译阶段中的任何测试源代码。
    - process-test-sources(处理测试源代码): 处理测试源代码，比如说，过滤任意值
    - generate-test-resources(生成测试资源文件): 为测试创建资源文件。
    - process-test-resources(处理试资源文件): 复制和处理测试资源到目标目录。
    - **test-compile**(编译测试源码): 编译测试源代码到测试目标目录
    - process-test-classes(处理测试类文件): 处理测试源码编译生成的文件
    - **test**(测试): 使用合适的单元测试框架运行测试，Juit是其中之一。
    - prepare-package(准备打包): 在实际打包之前，执行任何的必要的操作为打包做准备。
    - **package**(打包): 将编译后的代码打包成可分发格式的文件，比如JAR、WAR或者EAR文件
    - pre-integration-test(集成测试前): 在执行集成测试前进行必要的动作。比如说，搭建需要的环境。
    - integration-test(集成测试): 处理和部署项目到可以运行集成测试环境中
    - post-integration-test(集成测试后): 在执行集成测试完成后进行必要的动作。比如说，清理集成测试环境.
    - verify(验证): 运行任意的检查来验证项目包有效且达到质量标准
    - **install**(安装): 安装项目包到本地仓库，这样项目包可以用作其他本地项目的依赖。
    - deploy(部署): 将最终的项目包复制到远程仓库中与其他开发者和项目共享。
- site：产生报告，发布站点等
    - pre-site：执行一些需要在生成重点文档之前完成的工作
    - site：生成项目的站点文档
    - post-site：执行一些需要在生成站点文档后完成的工作，并且为部署做准备
    - site-deploy：将生成的站点文档部署到特定的服务器

插件：
- 插件与生命周期内的阶段绑定，在执行到对应生命周期时执行对应的插件功能。
- 默认 maven 在各个生命周期上绑定预设的功能
- 通过插件可以自定义其他功能

## 分模块开发与设计

Todo

