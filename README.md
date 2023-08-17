<p align="center">
	<img alt="logo" src="" width="150" height="150">
</p>
<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">Easy-Log</h1>
<h4 align="center">易用的日志收集、分析和检索平台</h4>
<p align="center">
	<a href="https://gitee.com/easy-log/easy-log/stargazers"><img src="https://gitee.com/easy-log/easy-log/badge/star.svg"></a>
	<a href="https://gitee.com/easy-log/easy-log/members"><img src="https://gitee.com/easy-log/easy-log/badge/fork.svg"></a>
    <a href="./LICENSE">
        <img src="https://img.shields.io/badge/license-MIT-red" alt="license MIT">
    </a>
</p>

---

> 项目特性

- 无代码入侵的分布式日志系统，基于log4j、log4j2、logback搜集日志，基于[TLog](https://tlog.yomahub.com)实现链路追踪，方便查询关联日志
- elasticsearch作为日志存储、查询分析引擎,利用Data Streams + ILM机制功能特性完成日志索引的生命周期管理
- 基于MQTT协议实现日志收集、实时修改应用日志级别、日志告警推送及日志实时过滤等功能

> 核心架构

![Easy-Log系统架构.jpg](doc/img/Easy-Log系统架构.jpg)

---

> 核心模块说明

- mqtt broker，负责消息推送，基于[EMQX](https://www.emqx.io/zh)中间件实现
- easy-log-admin负责用户认证、日志告警规则、日志实时过滤等基础信息管理
- easy-log-compute主要实现日志告警、日志实时过滤和日志收集速率计算等功能
- easy-log-collector主要负责订阅日志数据，批量插入ES集群

> Easy-Log部署

1. [ES集群部署](doc/quick-start/es/es.md)
2. [emqx集群部署](doc/quick-start/emqx/emqx.md)
3. [Easy-Log部署](doc/quick-start/easy-log/el.md)

---

> 客户端使用说明

- 创建客户端配置文件

```properties
# 创建src/main/resources/easy-log.properties
appName=demo                            #应用名称
namespace=test                          #命名空间
mqttAddress=tcp://ip:1883               #emqx地址
queueSize=10240                         #队列大小
maxPushSize=500                         #日志批推送大小
```

---

- 客户端maven依赖引入

```xml
<!--结合项目中使用的日志框架-->
<dependencies>
    <dependency>
        <groupId>com.chj</groupId>
        <artifactId>easy-log-[log4j2|log4j|logback]-appender</artifactId>
        <version>${latest}</version>
    </dependency>
    <!--tlog 
        一个轻量级的分布式日志标记追踪神器，10分钟即可接入，自动对日志打标签完成微服务的链路追踪
        基于tlog实现日志链路追踪，强烈建议引入该依赖
    -->
    <dependency>
        <groupId>com.yomahub</groupId>
        <artifactId>tlog-all-spring-boot-starter</artifactId>
        <version>${latest}</version>
    </dependency>
</dependencies>
```

> 修改日志配置文件

- logback 配置

```xml

<configuration scan="false" debug="false">
    <!--其他Appender-->
    <appender name="EASY_LOG" class="com.chj.easy.log.logback.appender.EasyLogAppender"/>
    <root level="INFO">
        <!--其他appender-ref-->
        <appender-ref ref="EASY_LOG"/>
    </root>
</configuration>
```

- log4j2 配置

```xml

<Configuration>
    <Appenders>
        <!--其他Appender-->
        <EasyLog name="EASY_LOG"/>
    </Appenders>

    <Loggers>
        <root level="info">
            <!--其他appender-ref-->
            <appender-ref ref="EASY_LOG"/>
        </root>
    </Loggers>
</Configuration>
```

- log4j 配置

```properties
log4j.appender.EASY_LOG=com.chj.easy.log.log4j.appender.EasyLogAppender
```
