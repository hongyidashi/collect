# SpringBoot日志框架

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [零、开篇](#%E9%9B%B6%E5%BC%80%E7%AF%87)
- [一、日志介绍](#%E4%B8%80%E6%97%A5%E5%BF%97%E4%BB%8B%E7%BB%8D)
    - [1. 日志级别](#1-%E6%97%A5%E5%BF%97%E7%BA%A7%E5%88%AB)
    - [2. 日志框架有哪些？](#2-%E6%97%A5%E5%BF%97%E6%A1%86%E6%9E%B6%E6%9C%89%E5%93%AA%E4%BA%9B)
- [二、Spring Boot 日志框架](#%E4%BA%8Cspring-boot-%E6%97%A5%E5%BF%97%E6%A1%86%E6%9E%B6)
    - [1. 代码中如何使用日志？](#1-%E4%BB%A3%E7%A0%81%E4%B8%AD%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8%E6%97%A5%E5%BF%97)
    - [2. 如何定制日志级别？](#2-%E5%A6%82%E4%BD%95%E5%AE%9A%E5%88%B6%E6%97%A5%E5%BF%97%E7%BA%A7%E5%88%AB)
    - [3. 日志如何输出到文件中？](#3-%E6%97%A5%E5%BF%97%E5%A6%82%E4%BD%95%E8%BE%93%E5%87%BA%E5%88%B0%E6%96%87%E4%BB%B6%E4%B8%AD)
    - [4. 如何定制日志格式？](#4-%E5%A6%82%E4%BD%95%E5%AE%9A%E5%88%B6%E6%97%A5%E5%BF%97%E6%A0%BC%E5%BC%8F)
- [四、如何自定义日志配置？](#%E5%9B%9B%E5%A6%82%E4%BD%95%E8%87%AA%E5%AE%9A%E4%B9%89%E6%97%A5%E5%BF%97%E9%85%8D%E7%BD%AE)
    - [1. configuration节点](#1-configuration%E8%8A%82%E7%82%B9)
    - [2. root节点](#2-root%E8%8A%82%E7%82%B9)
    - [3. contextName节点](#3-contextname%E8%8A%82%E7%82%B9)
    - [4. property节点](#4-property%E8%8A%82%E7%82%B9)
    - [5. appender节点](#5-appender%E8%8A%82%E7%82%B9)
    - [6. logger节点](#6-logger%E8%8A%82%E7%82%B9)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

[TOC]

## 零、开篇

日志通常不会在需求阶段作为一个功能单独提出来，也不会在产品方案中看到它的细节。但是，这丝毫不影响它在任何一个系统中的重要的地位。

今天就来介绍一下Spring Boot中的日志如何配置。

本文基于的Spring Boot的版本是`2.5.9`

## 一、日志介绍

### 1. 日志级别

几种常见的日志级别由低到高分为：`TRACE < DEBUG < INFO < WARN < ERROR < FATAL`。

如何理解这个日志级别呢？很简单，如果项目中的日志级别设置为`INFO`，那么比它更低级别的日志信息就看不到了，即是`TRACE`、`DEBUG`日志将会不显示。

### 2. 日志框架有哪些？

常见的日志框架有`log4j`、`logback`、`log4j2`。

`log4j`这个日志框架显示是耳熟能详了，在Spring开发中是经常使用，但是据说log4j官方已经不再更新了，而且在性能上比`logback`、`log4j2`差了很多。

`logback`是由`log4j`创始人设计的另外一个开源日志框架，logback相比之于log4j性能提升了10以上，初始化内存加载也更小了。作为的Spring Boot默认的日志框架肯定是有着不小的优势。

`log4j2`晚于`logback`推出，官网介绍性能比`logback`
高，但谁知道是不是王婆卖瓜自卖自夸，坊间流传，log4j2在很多思想理念上都是照抄logback，因此即便log4j2是Apache官方项目，Spring等许多框架项目没有将它纳入主流。**此处完全是作者道听途说，不必当真，题外话而已**。

日志框架很多，究竟如何选择能够适应现在的项目开发，当然不是普通程序员考虑的，但是为了更高的追求，至少应该了解一下，哈哈。

## 二、Spring Boot 日志框架

Spring Boot默认的日志框架是`logback`，既然Spring Boot能够将其纳入的默认的日志系统，肯定是有一定的考量的，因此实际开发过程中还是不要更换。

原则上需要使用logback,需要添加以下依赖，但是既然是默认的日志框架，当然不用重新引入依赖了。

```xml

<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-logging</artifactId>
```

Spring Boot中默认的日志级别是`INFO`，启动项目日志打印如下：

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.5.9)

2022-10-08 15:56:46.757  INFO 8069 --- [           main] org.collect.server1.Server1Application   : Starting Server1Application using Java 1.8.0_221 on DF41.local with PID 8069 (/Users/panhongtong/work/IDEA-workspace/collect/code/server-demo1/target/classes started by panhongtong in /Users/panhongtong/work/IDEA-workspace/collect)
2022-10-08 15:56:46.759  INFO 8069 --- [           main] org.collect.server1.Server1Application   : No active profile set, falling back to default profiles: default
2022-10-08 15:56:47.449  INFO 8069 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8821 (http)
2022-10-08 15:56:47.456  INFO 8069 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2022-10-08 15:56:47.456  INFO 8069 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.45]
2022-10-08 15:56:47.501  INFO 8069 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2022-10-08 15:56:47.501  INFO 8069 --- [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 699 ms
2022-10-08 15:56:47.628  INFO 8069 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2022-10-08 15:56:47.769  INFO 8069 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8821 (http) with context path ''
2022-10-08 15:56:47.777  INFO 8069 --- [           main] org.collect.server1.Server1Application   : Started Server1Application in 1.427 seconds (JVM running for 1.884)
```

从上图可以看出，输出的日志的默认元素如下：

1. 时间日期：精确到毫秒
2. 日志级别：ERROR, WARN, INFO, DEBUG , TRACE
3. 进程ID
4. 分隔符：— 标识实际日志的开始
5. 线程名：方括号括起来（可能会截断控制台输出）
6. Logger名：通常使用源代码的类名
7. 日志内容

### 1. 代码中如何使用日志？

在业务中肯定需要追溯日志，那么如何在自己的业务中输出日志呢？其实常用的有两种方式，下面一一介绍。

第一种其实也是很早之前常用的一种方式，只需要在代码添加如下：

```java
private final Logger logger=LoggerFactory.getLogger(DemoApplicationTests.class);
```

这种方式显然比较鸡肋，如果每个类中都添加一下岂不是很low。别着急，lombok为我们解决了这个难题。

要想使用lombok，需要添加如下依赖：

```xml

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

使用也是很简单，只需要在类上标注一个注解`@Slf4j`即可，如下：

```java

@Slf4j
class DemoApplicationTests {
    @Test
    public void test() {
        log.debug("输出DEBUG日志.......");
    }
}
```

### 2. 如何定制日志级别？

Spring Boot中默认的日志级别是INFO，但是可以自己定制日志级别，如下：

```properties
logging.level.root=DEBUG
```

上面是将所有的日志的级别都改成了`DEBUG`，Spring Boot还支持`package`级别的日志级别调整，格式为：`logging.level.xxx=xxx`，如下：

```properties
logging.level.com.example.demo=INFO
```

那么完整的配置如下：

```properties
logging.level.root=DEBUG
logging.level.com.example.demo=INFO
```

### 3. 日志如何输出到文件中？

Spring Boot中日志默认是输出到控制台的，但是在生产环境中显示不可行的，因此需要配置日志输出到日志文件中。

其中有两个重要配置如下：

1. `logging.file.path`：指定日志文件的路径
2. `logging.file.name`：日志的文件名，默认为`spring.log`

注意：官方文档说这两个属性不能同时配置，否则不生效，因此只需要配置一个即可。

指定输出的文件为当前项目路径的`logs`文件下，默认生成的日志文件为`spring.log`，如下：

```properties
logging.file.path=./logs
```

**日志文件中还有一些其他的属性，比如日志文件的最大size，保留几天的日志等等，下面会介绍到。**

### 4. 如何定制日志格式？

默认的日志格式在第一张图已经看到了，有时我们需要定制自己需要的日志输出格式，这样在排查日志的时候能够一目了然。

定制日志格式有两个配置，分别是控制台的输出格式和文件中的日志输出格式，如下：

1. `logging.pattern.console`：控制台的输出格式
2. `logging.pattern.file`：日志文件的输出格式

例如配置如下：

```properties
logging.pattern.console=%d{yyyy/MM/dd-HH:mm:ss} [%thread] %-5level %logger- %msg%n
logging.pattern.file=%d{yyyy/MM/dd-HH:mm} [%thread] %-5level %logger- %msg%n
```

上面的配置编码的含义如下：

```
%d{HH:mm:ss.SSS}——日志输出时间

%thread——输出日志的进程名字，这在Web应用以及异步任务处理中很有用

%-5level——日志级别，并且使用5个字符靠左对齐

%logger- ——日志输出者的名字

%msg——日志消息

%n——平台的换行符
```

## 四、如何自定义日志配置？

Spring Boot官方文档指出，根据不同的日志系统，可以按照如下的日志配置文件名就能够被正确加载，如下：

1. **`Logback`**：logback-spring.xml, logback-spring.groovy, logback.xml, logback.groovy
2. **`Log4j`**：log4j-spring.properties, log4j-spring.xml, log4j.properties, log4j.xml
3. **`Log4j2`**：log4j2-spring.xml, log4j2.xml
4. **`JDK (Java Util Logging)`**：logging.properties

Spring Boot官方推荐优先使用带有-spring的文件名作为你的日志配置。因此只需要在`src/resources`文件夹下创建`logback-spring.xml`即可，配置文件内容如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="60 seconds" debug="false">
    <!-- 定义日志存放目录 -->
    <property name="logPath" value="logs"/>
    <!--    日志输出的格式-->
    <property name="PATTERN" value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t-%L] %-5level %logger{36} %L %M - %msg%xEx%n"/>
    <contextName>logback</contextName>

    <!--输出到控制台 ConsoleAppender-->
    <appender name="consoleLog" class="ch.qos.logback.core.ConsoleAppender">
        <!--展示格式 layout-->
        <layout class="ch.qos.logback.classic.PatternLayout">
            <pattern>${PATTERN}</pattern>
        </layout>
        <!--过滤器，只有过滤到指定级别的日志信息才会输出，如果level为ERROR，那么控制台只会输出ERROR日志-->
        <!--        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">-->
        <!--            <level>ERROR</level>-->
        <!--        </filter>-->
    </appender>

    <!--正常的日志文件，输出到文件中-->
    <appender name="fileDEBUGLog" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--如果只是想要 Info 级别的日志，只是过滤 info 还是会输出 Error 日志，因为 Error 的级别高，
        所以我们使用下面的策略，可以避免输出 Error 的日志-->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <!--过滤 Error-->
            <level>Error</level>
            <!--匹配到就禁止-->
            <onMatch>DENY</onMatch>
            <!--没有匹配到就允许-->
            <onMismatch>ACCEPT</onMismatch>
        </filter>

        <!--日志名称，如果没有File 属性，那么只会使用FileNamePattern的文件路径规则
            如果同时有<File>和<FileNamePattern>，那么当天日志是<File>，明天会自动把今天
            的日志改名为今天的日期。即，<File> 的日志都是当天的。
        -->
        <File>${logPath}/log_demo.log</File>
        <!--滚动策略，按照时间滚动 TimeBasedRollingPolicy-->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--文件路径,定义了日志的切分方式——把每一天的日志归档到一个文件中,以防止日志填满整个磁盘空间-->
            <FileNamePattern>${logPath}/log_demo_%d{yyyy-MM-dd}.log</FileNamePattern>
            <!--只保留最近90天的日志-->
            <maxHistory>90</maxHistory>
            <!--用来指定日志文件的上限大小，那么到了这个值，就会删除旧的日志-->
            <!--<totalSizeCap>1GB</totalSizeCap>-->
        </rollingPolicy>
        <!--日志输出编码格式化-->
        <encoder>
            <charset>UTF-8</charset>
            <pattern>${PATTERN}</pattern>
        </encoder>
    </appender>

    <!--输出ERROR日志到指定的文件中-->
    <appender name="fileErrorLog" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--如果只是想要 Error 级别的日志，那么需要过滤一下，默认是 info 级别的，ThresholdFilter-->
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>Error</level>
        </filter>
        <!--日志名称，如果没有File 属性，那么只会使用FileNamePattern的文件路径规则
            如果同时有<File>和<FileNamePattern>，那么当天日志是<File>，明天会自动把今天
            的日志改名为今天的日期。即，<File> 的日志都是当天的。
        -->
        <File>${logPath}/error.log</File>
        <!--滚动策略，按照时间滚动 TimeBasedRollingPolicy-->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--文件路径,定义了日志的切分方式——把每一天的日志归档到一个文件中,以防止日志填满整个磁盘空间-->
            <FileNamePattern>${logPath}/error_%d{yyyy-MM-dd}.log</FileNamePattern>
            <!--只保留最近90天的日志-->
            <maxHistory>90</maxHistory>
            <!--用来指定日志文件的上限大小，那么到了这个值，就会删除旧的日志-->
            <!--<totalSizeCap>1GB</totalSizeCap>-->
        </rollingPolicy>
        <!--日志输出编码格式化-->
        <encoder>
            <charset>UTF-8</charset>
            <pattern>${PATTERN}</pattern>
        </encoder>
    </appender>


    <!--指定最基础的日志输出级别-->
    <root level="DEBUG">
        <!--appender将会添加到这个loger-->
        <appender-ref ref="consoleLog"/>
        <appender-ref ref="fileDEBUGLog"/>
        <appender-ref ref="fileErrorLog"/>
    </root>

    <!--    定义指定package的日志级别-->
    <logger name="org.springframework" level="DEBUG"></logger>
    <logger name="org.mybatis" level="DEBUG"></logger>
    <logger name="java.sql.Connection" level="DEBUG"></logger>
    <logger name="java.sql.Statement" level="DEBUG"></logger>
    <logger name="java.sql.PreparedStatement" level="DEBUG"></logger>
    <logger name="io.lettuce.*" level="INFO"></logger>
    <logger name="io.netty.*" level="ERROR"></logger>
    <logger name="com.rabbitmq.*" level="DEBUG"></logger>
    <logger name="org.springframework.amqp.*" level="DEBUG"></logger>
    <logger name="org.springframework.scheduling.*" level="DEBUG"></logger>
    <!--定义com.xxx..xx..xx包下的日志信息不上传，直接输出到fileDEBUGLog和fileErrorLog这个两个appender中，日志级别为DEBUG-->
    <logger name="com.xxx.xxx.xx" additivity="false" level="DEBUG">
        <appender-ref ref="fileDEBUGLog"/>
        <appender-ref ref="fileErrorLog"/>
    </logger>

</configuration>
```

当然，如果就不想用Spring Boot推荐的名字，想自己定制也行，只需要在配置文件中指定配置文件名即可，如下：

```
logging.config=classpath:logging-config.xml
```

**懵逼了，一堆配置什么意思？别着急，下面一一介绍。**

### 1. configuration节点

这是一个根节点，其中的各个属性如下：

1. `scan`：当此属性设置为true时，配置文件如果发生改变，将会被重新加载，默认值为true。
2. `scanPeriod`：设置监测配置文件是否有修改的时间间隔，如果没有给出时间单位，默认单位是毫秒。当scan为true时，此属性生效。默认的时间间隔为1分钟。
3. `debug`：当此属性设置为true时，将打印出logback内部日志信息，实时查看logback运行状态。默认值为false。

### 2. root节点

这是一个必须节点，用来指定基础的日志级别，只有一个`level`属性，默认值是`DEBUG`。该节点可以包含零个或者多个元素，子节点是`appender-ref`，标记这个`appender`将会添加到这个logger中。

### 3. contextName节点

标识一个上下文名称，默认为default，一般用不到

### 4. property节点

标记一个上下文变量，属性有name和value，定义变量之后可以使用`${}`来获取。

### 5. appender节点

用来格式化日志输出节点，有两个属性`name`和`class`，class用来指定哪种输出策略，常用就是**控制台输出策略**和**文件输出策略**。

这个节点很重要，通常的日志文件需要定义三个appender，分别是控制台输出，常规日志文件输出，异常日志文件输出。

该节点有几个重要的子节点，如下：

1. `filter`：日志输出拦截器，没有特殊定制一般使用系统自带的即可，但是如果要将日志分开，比如将ERROR级别的日志输出到一个文件中，将除了`ERROR`级别的日志输出到另外一个文件中，此时就要拦截`ERROR`级别的日志了。
2. `encoder`：和pattern节点组合用于具体输出的日志格式和编码方式。
3. `file`: 节点用来指明日志文件的输出位置，可以是绝对路径也可以是相对路径
4. `rollingPolicy`: 日志回滚策略，在这里我们用了TimeBasedRollingPolicy，基于时间的回滚策略，有以下子节点fileNamePattern，必要节点，可以用来设置指定时间的日志归档。
5. `maxHistory` : 可选节点，控制保留的归档文件的最大数量，超出数量就删除旧文件,，例如设置为30的话，则30天之后，旧的日志就会被删除
6. `totalSizeCap`: 可选节点，用来指定日志文件的上限大小，例如设置为3GB的话，那么到了这个值，就会删除旧的日志

### 6. logger节点

可选节点，用来具体指明包的日志输出级别，它将会覆盖root的输出级别。该节点有几个重要的属性如下：

1. `name`：指定的包名
2. `level`：可选，日志的级别
3. `addtivity`：可选，默认为true，将此logger的信息向上级传递，将有root节点定义日志打印。如果设置为false，将不会上传，此时需要定义一个`appender-ref`节点才会输出。