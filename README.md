# collect

### 介绍

学习资料整理，内容均为**抄袭、整合**

## 目录

### Java基础

+ JavaSE
    - [序列化](note/javase/serialization.md)  最后更新于2021-1-3
    - [伪共享](note/javase/falsesharing.md)  最后更新于2021-3-11
    - [JDK自带的观察者模式](note/javase/JDK自带的观察者模式实现.md)  最后更新于2021-4-10
    - [Function使用技巧](note/javase/Function使用技巧.md)  最后更新于2021-9-25
    - [Java基础面试题](note/javase/Java基础面试题.md)  最后更新于2021-10-12
    - [JavaAgent-探针技术](note/javase/JavaAgent-探针技术.md)  最后更新于2022-4-19

### Java高级

+ JVM
    - GC
        - [G1垃圾收集器](note/jvm/gc/G1垃圾收集器.md)  最后更新于2021-1-29
        - [TLAB](note/jvm/gc/TLAB.md)  最后更新于2021-2-5
        - [三色标记](note/jvm/gc/三色标记.md)  最后更新于2021-5-29
        - [三色标记详解](note/jvm/gc/三色标记详解.md)  最后更新于2021-12-15
        - [Stop The World 是何时发生的](note/jvm/gc/StopTheWorld是何时发生的.md)  最后更新于2021-9-13
        - [12张图带你彻底理解ZGC](note/jvm/gc/12张图带你彻底理解ZGC.md)  最后更新于2022-3-25
    - JVM深入
        - [JVM深入-基础概念](note/jvm/jvm-deepen/JVM基础概念深入.md)  最后更新于2021-4-22
        - [JVM深入-Java锁机制.md](note/jvm/jvm-deepen/JVM深入-Java锁机制.md)  最后更新于2021-5-15
        - [JVM深入-Java对象内存布局](note/jvm/jvm-deepen/JVM深入-Java对象内存布局.md)  最后更新于2021-5-5
        - [JVM深入-Synchronized锁升级](note/jvm/jvm-deepen/JVM深入-Synchronized锁升级.md)  最后更新于2021-5-9
        - [JVM深入-Unsafe类详解](note/jvm/jvm-deepen/JVM深入-Unsafe类详解.md)  最后更新于2021-5-18
        - [JVM深入-JNI到底是个啥](note/jvm/jvm-deepen/JVM深入-JNI到底是个啥.md)  最后更新于2021-5-18
        - [JVM深入-JVM运行时数据区](note/jvm/jvm-deepen/JVM深入-JVM运行时数据区.md)  最后更新于2021-5-24

    - 面试题
        - [JVM21问](note/jvm/interview/JVM21问.md)  最后更新于2021-9-4


+ 多线程和并发
    - [异步编程CompletableFuture](note/multithreading/async/CompletableFuture.md)  最后更新于2021-1-2
    - [阻塞队列](note/multithreading/queue/BlockingQueue.md)  最后更新于2021-1-9
    - [66个并发多线程基础知识](note/multithreading/interview/66个并发多线程基础知识.md)  最后更新于2021-5-9
    - [16张图解开AbstractQueuedSynchronizer](note/multithreading/juc/16张图解开AbstractQueuedSynchronizer.md)  最后更新于2021-5-19
    - [为什么Java线程没有Running状态](note/multithreading/interview/为什么Java线程没有Running状态.md)  最后更新于2021-5-20
    - [剖析ReentrantReadWriteLock源码](note/multithreading/juc/剖析ReentrantReadWriteLock源码.md)  最后更新于2021-7-6
    - [ThreadLocal机制](note/multithreading/juc/ThreadLocal机制.md)  最后更新于2021-9-14
    - [FastThreadLocal](note/multithreading/juc/FastThreadLocal.md)  最后更新于2021-9-14
    - [TransmittableThreadLocal](note/multithreading/juc/TransmittableThreadLocal.md)  最后更新于2021-9-30
    - [线程池使用案例-多线程永动任务](note/multithreading/async/线程池使用案例-多线程永动任务.md)  最后更新于2022-9-28

+ 代码优化
    - [代码优化细节](note/optimization/代码优化细节.md)  最后更新于2021-5-12
    - [聊聊接口性能优化的11个小技巧](note/optimization/聊聊接口性能优化的11个小技巧.md)  最后更新于2021-11-28

### 数据库

+ MySQL
    - [MySQL大杂烩](note/mysql/MySQL大杂烩.md)  最后更新于2021-2-3
    - [InnoDB解决幻读的方案--LBCC&MVCC](note/mysql/transaction/InnoDB解决幻读的方案--LBCC&MVCC.md)  最后更新于2021-6-1
    - [攻克order_by](note/mysql/攻克order_by.md)  最后更新于2021-6-14
    - [主从延迟](note/mysql/主从延迟.md)  最后更新于2021-8-1
    - [Buffer Pool](note/mysql/BufferPool.md)  最后更新于2021-8-19
    - [innodb是如何存数据的](note/mysql/innodb是如何存数据的.md)  最后更新于2021-9-2
    - [MySQL日志15连问](note/mysql/MySQL日志15连问.md)  最后更新于2021-9-28
    - [MySQL基础30问](note/mysql/MySQL基础30问.md)  最后更新于2021-10-3
    - [MySQL是这样执行的](note/mysql/MySQL是这样执行的.md)  最后更新于2021-10-4
    - [explain-索引优化](note/mysql/index/explain-索引优化.md)  最后更新于2021-11-30
    - [Innodb到底是怎么加锁的](note/mysql/lock/Innodb到底是怎么加锁的.md)  最后更新于2021-12-14
    - [聊聊数据页变成索引这件事](note/mysql/index/聊聊数据页变成索引这件事.md)  最后更新于2022-3-5
    - [读写分离从库读到新值主库读到旧值](note/mysql/ms/读写分离从库读到新值主库读到旧值.md)  最后更新于2022-3-5
    - [ChangeBuffer是如何提升索引性能的](note/mysql/index/ChangeBuffer是如何提升索引性能的.md)  最后更新于2022-3-23
    - [为什么MySQL数据库单表最大是两千万](note/mysql/index/为什么MySQL数据库单表最大是两千万.md)  最后更新于2022-4-18
    - [MySQL的所有Buffer](note/mysql/MySQL的所有Buffer.md)  最后更新于2022-9-20
    - [MySQL六十六问](note/mysql/MySQL六十六问.md)  最后更新于2022-9-21
    - [MySQL慢查询优化](note/mysql/optimization/MySQL慢查询优化.md)  最后更新于2022-9-25
    - [MySQL幻读被彻底解决了吗](note/mysql/transaction/MySQL幻读被彻底解决了吗.md)  最后更新于2022-9-26

+ Redis
    - [Redis主从复制](note/redis/Redis主从复制.md)  最后更新于2020-12-15
    - [Redis分布式锁五种方案](note/redis/Redis分布式锁五种方案.md)  最后更新于2021-6-7
    - [Redis为什么这么快](note/redis/Redis为什么这么快.md)  最后更新于2021-8-3
    - [Redis高可用-Cluster集群](note/redis/Redis高可用-Cluster集群.md)  最后更新于2021-8-5
    - [Gossip协议-通俗版](note/redis/Gossip协议-通俗版.md)  最后更新于2021-8-6
    - [一致性哈希算法-简单版](note/redis/一致性哈希算法-简单版.md)  最后更新于2021-8-18
    - [Redis配置文件-之一](note/redis/Redis配置文件-之一.md)  最后更新于2021-9-6
    - [Redis配置文件-之二](note/redis/Redis配置文件-之二.md)  最后更新于2021-9-6
    - [Redis实战-Bitmap实现统计](note/redis/Redis实战-Bitmap实现统计.md)  最后更新于2021-11-11
    - [Redis实战-巧用数据类型实现亿级数据统计](note/redis/Redis实战-巧用数据类型实现亿级数据统计.md)  最后更新于2021-11-12
    - [Redis实战-Geo数据类型实现附近的人](note/redis/Redis实战-Geo数据类型实现附近的人.md)  最后更新于2021-11-14
    - [Redis缓存使用技巧和设计方案](note/redis/Redis缓存使用技巧和设计方案.md)  最后更新于2021-11-28
    - [Redis分布式锁的8大坑](note/redis/Redis分布式锁的8大坑.md)  最后更新于2021-12-10
    - [Redis大key的处理](note/redis/Redis大key的处理.md)  最后更新于2022-10-9
    - [Redis是单线程的进程吗](note/redis/Redis是单线程的进程吗.md)  最后更新于2022-10-10

+ Elasticsearch
    - [Elasticsearch如何做到快速检索](note/es/Elasticsearch如何做到快速检索.md)  最后更新于2021-9-1

+ 分库分表
    - [为什么要分库分表](note/ss/为什么要分库分表.md)  最后更新于2021-12-1

### 网络编程

+ 图解网络
    - [图解网络-摸清网络的第一步](note/net/diagram/图解网络-摸清网络的第一步.md)  最后更新于2021-5-30
    - [图解网络-一个数据包在网络中的心路历程](note/net/diagram/图解网络-一个数据包在网络中的心路历程.md)  最后更新于2021-5-31
    - [图解网络-ping的工作原理](note/net/diagram/图解网络-ping的工作原理.md)  最后更新于2021-5-31
    - [图解网络-TCP和UDP](note/net/diagram/图解网络-TCP和UDP.md)  最后更新于2021-6-26
    - [图解网络-TCP三次握手和四次挥手面试题](note/net/diagram/图解网络-TCP三次握手和四次挥手面试题.md)  最后更新于2021-6-29
    - [图解网络-复杂的TCP](note/net/diagram/图解网络-复杂的TCP.md)  最后更新于2021-6-30
    - [图解网络-IP基础知识](note/net/diagram/图解网络-IP基础知识.md)  最后更新于2021-7-2
    - [图解网络-TCP半连接队列和全连接队列](note/net/diagram/图解网络-TCP半连接队列和全连接队列.md)  最后更新于2021-7-5
    - [图解网络-TCP面向字节流协议](note/net/diagram/图解网络-TCP面向字节流协议.md)  最后更新于2021-10-5
    - [图解网络-Keepalive](note/net/diagram/图解网络-Keepalive.md)  最后更新于2021-10-13

+ 其他系列
    - [重放攻击](note/net/other/重放攻击.md)  最后更新于2021-7-31
    - [CDN基础](note/net/other/CDN基础.md)  最后更新于2021-10-4
    - [37张图详解MAC地址、以太网、二层转发、VLAN](note/net/diagram2/37张图详解MAC地址、以太网、二层转发、VLAN.md)  最后更新于2021-11-27

+ I/O
    - [零拷贝和IO模型](note/io/零拷贝和IO模型.md)  最后更新于2020-12-23
    - netty
        - [Netty模型架构](note/io/netty/Netty模型架构.md)  最后更新于2021-5-27
        - [Netty基础-Reactor模型](note/io/netty/Netty基础-Reactor模型.md)  最后更新于2021-5-27
        - [Netty基础-Netty线程模型](note/io/netty/Netty基础-Netty线程模型.md)  最后更新于2021-5-27
        - [Netty基础-Netty核心api](note/io/netty/Netty基础-Netty核心api.md)  最后更新于2021-5-28
        - [Netty基础-Netty异步模型](note/io/netty/Netty基础-Netty异步模型.md)  最后更新于2021-5-28
    - netty2
        - [NIO之三大核心简介](note/io/netty2/nio基础之三大核心简介.md)  最后更新于2022-4-20
        - [NIO之ByteBuffer](note/io/netty2/nio之ByteBuffer.md)  最后更新于2022-4-22
        - [NIO之文件编程](note/io/netty2/nio之文件编程.md)  最后更新于2022-4-24
        - [NIO之网络编程](note/io/netty2/nio之网络编程.md)  最后更新于2022-4-25
        - [初识Netty-EventLoop介绍](note/io/netty2/初识Netty-EventLoop介绍.md)  最后更新于2022-4-26
        - [初识Netty-Channel](note/io/netty2/初识Netty-Channel.md)  最后更新于2022-4-26
        - [初识Netty-Future&Promise](note/io/netty2/初识Netty-Future&Promise.md)  最后更新于2022-4-26

+ HTTP
    - [HTTPS基础](note/http/HTTPS基础.md)  最后更新于2020-12-23
    - [HTTP1.1](note/http/HTPP1.1.md)  最后更新于2021-4-3
    - [HTTP2](note/http/HTTP2.md)  最后更新于2021-4-3
    - [30张图解HTTP常见面试题](note/http/30张图解HTTP常见的面试题.md)  最后更新于2021-5-29
    - [优化HTTP1.1](note/http/优化HTTP1.1.md)  最后更新于2021-6-2
    - [HTTPS入门](note/http/HTTPS入门.md)  最后更新于2021-6-22
    - [优化HTTPS的手段](note/http/优化HTTPS的手段.md)  最后更新于2021-6-21
    - [ECDHE算法](note/http/ECDHE算法.md)  最后更新于2021-6-23
    - [QUIC协议](note/http/QUIC协议.md)  最后更新于2021-6-25

+ WebSocket
    - [WebSocket实现长连接](note/websocket/WebSocket实现长连接.md)  最后更新于2020-12-28

### 计算机组成

+ 图解计算机
    - [图解计算机-CPU执行程序过程](note/computer/diagram/CPU执行程序过程.md)  最后更新于2021-6-2
    - [图解计算机-存储器的层次结构](note/computer/diagram/存储器的层次结构.md)  最后更新于2021-6-3
    - [图解计算机-CPU缓存一致性](note/computer/diagram/CPU缓存一致性.md)  最后更新于2021-6-17
    - [图解计算机-CPU执行任务过程](note/computer/diagram/CPU执行任务过程.md)  最后更新于2021-6-18
    - [图解计算机-float](note/computer/diagram/float.md)  最后更新于2021-6-20

### 操作系统

+ 图解操作系统
    - [图解操作系统-内存管理](note/os/diagram/内存管理.md)  最后更新于2021-6-4
    - [图解操作系统-进程和线程基础知识](note/os/diagram/进程和线程基础知识.md)  最后更新于2021-6-6
    - [图解操作系统-线程竞争与协作](note/os/diagram/线程竞争与协作.md)  最后更新于2021-6-6
    - [图解操作系统-进程间通信](note/os/diagram/进程间通信.md)  最后更新于2021-6-7
    - [图解操作系统-文件系统](note/os/diagram/文件系统.md)  最后更新于2021-6-8
    - [图解操作系统-输入输出设备](note/os/diagram/输入输出设备.md)  最后更新于2021-6-9
    - [图解操作系统-调度算法](note/os/diagram/调度算法.md)  最后更新于2021-6-10
    - [图解操作系统-零拷贝](note/os/diagram/零拷贝.md)  最后更新于2021-6-11
    - [图解操作系统-IO多路复用](note/os/diagram/IO多路复用.md)  最后更新于2021-6-12
    - [图解操作系统-Reactor和Proactor](note/os/diagram/Reactor和Proactor.md)  最后更新于2021-6-13
    - [图解操作系统-Linux内核vsWindows内核](note/os/diagram/Linux内核vsWindows内核.md)  最后更新于2021-6-14
    - [图解操作系统-线程间共享资源](note/os/diagram/线程间共享资源.md)  最后更新于2021-6-15
    - [图解操作系统-申请内存](note/os/diagram/申请内存.md)  最后更新于2021-6-16
    - [图解操作系统-虚拟内存](note/os/diagram/虚拟内存.md)  最后更新于2021-8-18
    - [图解操作系统-中断](note/os/diagram/中断.md)  最后更新于2021-8-24
    - [图解操作系统-内存满了会发生什么](note/os/diagram/内存满了会发生什么.md)  最后更新于2022-10-14
    - [图解操作系统-如何避免预读失效和缓存污染的问题](note/os/diagram/如何避免预读失效和缓存污染的问题.md)  最后更新于2022-10-15

+ 高速缓存与一致性
    - [Cache的基本原理](note/os/diagram2/Cache的基本原理.md)  最后更新于2022-10-9
    - [Cache组织方式](note/os/diagram2/Cache组织方式.md)  最后更新于2022-10-10
    - [TLB原理](note/os/diagram2/TLB原理.md)  最后更新于2022-10-13

[//]: # (    - [Cache和DMA一致性]&#40;note/os/diagram2/Cache和DMA一致性.md&#41;  最后更新于2022-10-16)

[//]: # (    - [iCache和dCache一致性]&#40;note/os/diagram2/iCache和dCache一致性.md&#41;  最后更新于2022-10-17)

[//]: # (    - [多核Cache一致性]&#40;note/os/diagram2/多核Cache一致性.md&#41;  最后更新于2022-10-18)

### 框架

+ Spring
    - [Spring的一些常见问题](note/spring/Spring的一些常见问题.md)  最后更新于2022-9-25
    - [Spring使用技巧](note/spring/spring使用技巧.md)  最后更新于2021-3-14
    - [Spring事务管理](note/spring/spring事务管理.md)  最后更新于2021-6-23
    - [Spring事务管理-传播特性](note/spring/spring事务管理-传播特性.md)  最后更新于2021-10-26
    - [Spring-AOP](note/spring/Spring-AOP.md)  最后更新于2021-11-2
    - [Spring大事务问题到底要如何解决](note/spring/Spring大事务问题到底要如何解决.md)  最后更新于2021-11-30
    - [Spring中Bean的生命周期](note/spring/Spring中Bean的生命周期.md)  最后更新于2022-3-25
    - [深入浅出Spring架构设计](note/spring/深入浅出Spring架构设计.md)  最后更新于2022-4-11
    - [SpringBoot扩展点](note/spring/SpringBoot扩展点.md)  最后更新于2022-9-18
    - [控制SpringBoot中bean的加载顺序](note/spring/控制SpringBoot中bean的加载顺序.md)  最后更新于2022-9-19

+ Spring Security
    - [Security跨域问题](note/security/Security跨域问题.md)  最后更新于2021-1-19
    - [知识点](note/security/知识点.md)  最后更新于2021-1-19

+ Mybatis
    - [手撸Mybatis-01-创建简单的映射器代理工厂](note/mybatis/手撸Mybatis-01-创建简单的映射器代理工厂.md)  最后更新于2022-5-18
    - [手撸Mybatis-02-实现映射器的注册和使用](note/mybatis/手撸Mybatis-02-实现映射器的注册和使用.md)  最后更新于2022-5-19
    - [手撸Mybatis-03-Mapper-XML的解析和注册使用](note/mybatis/手撸Mybatis-03-Mapper-XML的解析和注册使用.md)  最后更新于2022-5-21
    - [手撸Mybatis-04-数据源的解析、创建和使用](note/mybatis/手撸Mybatis-04-数据源的解析、创建和使用.md)  最后更新于2022-5-24
    - [手撸Mybatis-05-数据源池化技术实现](note/mybatis/手撸Mybatis-05-数据源池化技术实现.md)  最后更新于2022-5-26
    - [手撸Mybatis-06-SQL执行器的定义和实现](note/mybatis/手撸Mybatis-06-SQL执行器的定义和实现.md)  最后更新于2022-5-26

+ 微服务
    - 分布式事务
        - [分布式事务基本原理](note/microservice/transaction/分布式事务基本原理.md)  最后更新于2020-12-25
        - [Seata分布式事务AT模式源码解读](note/microservice/transaction/Seata分布式事务AT模式源码解读.md)  最后更新于2021-5-29
        - [分布式事务的七种解决方案](note/microservice/transaction/分布式事务的七种解决方案.md)  最后更新于2021-8-1

    - 远程调用
        - [Feign调用请求头丢失问题](note/microservice/rpc/Feign调用请求头丢失问题.md)  最后更新于2021-2-19
        - [微服务之间的最佳调用方式](note/microservice/rpc/微服务之间的最佳调用方式.md)  最后更新于2021-6-28

    - 服务发现
        - [浅谈Kubernetes中的服务发现](note/microservice/discovery/浅谈Kubernetes中的服务发现.md)  最后更新于2021-5-13

    - 熔断降级
        - [熔断降级原理](note/microservice/fuse/熔断降级原理.md)  最后更新于2021-11-10
        - [微服务和API网关限流熔断实现关键逻辑思路](note/microservice/fuse/微服务和API网关限流熔断实现关键逻辑思路.md)  最后更新于2022-1-5

    - 其他
        - [微服务拆分之道](note/microservice/other/微服务拆分之道.md)  最后更新于2021-6-20

+ 中间件
    - 消息队列
        - [MQ消息丢失、重复消费、有序性、堆积](note/middleware/mq/MQ消息丢失、重复消费、有序性、堆积.md)  最后更新于2020-12-28
        - [面试角度学习Kafka](note/middleware/mq/面试角度学习Kafka.md)  最后更新于2021-4-28
        - [避免消息重复消费](note/middleware/mq/避免消息重复消费.md)  最后更新于2021-12-16

+ 开箱即用类
    - [Disruptor-入门](note/other-frame/disruptor-入门.md)  最后更新于2021-9-22
    - [Disruptor-Disruptor类分析](note/other-frame/disruptor-Disruptor类分析.md)  最后更新于2021-9-22
    - [Nginx扩展-clojure基础](note/other-frame/Nginx扩展-clojure基础.md)  最后更新于2022-4-28
    - [Nginx扩展-五大handler](note/other-frame/Nginx扩展-五大handler.md)  最后更新于2022-4-28
    - [Nginx扩展-两大filter](note/other-frame/Nginx扩展-两大filter.md)  最后更新于2022-4-29
    - [Guava工具类-Map](note/other-frame/Guava工具类-Map.md)  最后更新于2022-5-16
    - [Servlet长轮询](note/other-frame/Servlet长轮询.md)  最后更新于2022-8-25
    - [SpringBoot日志框架](note/other-frame/SpringBoot日志框架.md)  最后更新于2022-10-8

### Kubernetes

- [k8s面试题](note/k8s/k8s面试题.md)  最后更新于2021-6-25

### 项目构建

+ maven
    - [Maven的核心概念和最佳实践](note/projectbuild/maven/Maven的核心概念和最佳实践.md)  最后更新于2022-3-5

### 领域驱动设计-DDD

+ [DDD-面向对象思想](note/ddd/DDD-面向对象思想.md)  最后更新于2021-4-4

### 综合面试题

+ [2021-虾皮服务端15连问](note/interview/2021-虾皮服务端15连问.md)  最后更新于2022-1-4
+ [2022-社招后端(三年工作经验一面)](note/interview/2022-社招后端(三年工作经验一面).md)  最后更新于2022-3-9

### 不推荐阅读

- [~~JDK9新特性~~](note/jdk/jdk9/新特性.md)(弃坑)
- [~~45个GIT经典操作场景，专治不会合代码~~](note/projectbuild/git/45个GIT经典操作场景，专治不会合代码.md)  最后更新于2022-3-23(弃坑)
- [~~定时器-Quartz~~](note/other-frame/quartz.md)  最后更新于2020-12-29(弃坑)
- [~~TCP~~](note/io/nio/NIO系列-TCP.md)  最后更新于2021-4-3(有用内容过少)
- [~~MySQL面试题~~](note/mysql/面试题.md)  最后更新于2021-1-30(有用内容过少)
- [~~ForkJoinPool~~](note/multithreading/async/ForkJoinPool.md)  最后更新于2021-4-8(弃坑)
- [~~Seata-AT模式简介~~](note/microservice/transaction/Seata-AT模式简介.md)  最后更新于2020-12-25(有用内容过少)

生成目录命令: doctoc