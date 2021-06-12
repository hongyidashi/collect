# collect

#### 介绍
学习资料整理，内容均为**抄袭、整合**


## 目录
### Java基础
+ JDK
  - JDK9
    - [新特性](note/jdk/jdk9/新特性.md)

+ JavaSE
  - [序列化](note/javase/serialization.md)  最后更新于2021-1-3  
  - [伪共享](note/javase/falsesharing.md)  最后更新于2021-3-11  
  - [JDK自带的观察者模式](note/javase/JDK自带的观察者模式实现.md)  最后更新于2021-4-10  

### Java高级
+ JVM
  - GC
    - [G1垃圾收集器](note/jvm/gc/G1垃圾收集器.md)  最后更新于2021-1-29  
    - [TLAB](note/jvm/gc/TLAB.md)  最后更新于2021-2-5  
    - [三色标记](note/jvm/gc/三色标记.md)  最后更新于2021-5-29  
  - JVM深入  
    - [JVM深入-基础概念](note/jvm/jvm-deepen/JVM基础概念深入.md)  最后更新于2021-4-22  
    - [JVM深入-Java锁机制.md](note/jvm/jvm-deepen/JVM深入-Java锁机制.md)  最后更新于2021-5-15  
    - [JVM深入-Java对象内存布局](note/jvm/jvm-deepen/JVM深入-Java对象内存布局.md)  最后更新于2021-5-5  
    - [JVM深入-Synchronized锁升级](note/jvm/jvm-deepen/JVM深入-Synchronized锁升级.md)  最后更新于2021-5-9  
    - [JVM深入-Unsafe类详解](note/jvm/jvm-deepen/JVM深入-Unsafe类详解.md)  最后更新于2021-5-18  
    - [JVM深入-JNI到底是个啥](note/jvm/jvm-deepen/JVM深入-JNI到底是个啥.md)  最后更新于2021-5-18  
    - [JVM深入-JVM运行时数据区](note/jvm/jvm-deepen/JVM深入-JVM运行时数据区.md)  最后更新于2021-5-24  

+ 多线程和并发
  - [异步编程CompeletableFuture](note/multithreading/async/CompeletableFuture.md)  最后更新于2021-1-2  
  - [ForkJoinPool](note/multithreading/async/ForkJoinPool.md)  最后更新于2021-4-8  
  - [阻塞队列](note/multithreading/queue/BlockingQueue.md)  最后更新于2021-1-9  
  - [66个并发多线程基础知识](note/multithreading/interview/66个并发多线程基础知识.md)  最后更新于2021-5-9  
  - [16张图解开AbstractQueuedSynchronizer](note/multithreading/juc/16张图解开AbstractQueuedSynchronizer.md)  最后更新于2021-5-19  
  - [为什么Java线程没有Running状态](note/multithreading/interview/为什么Java线程没有Running状态.md)  最后更新于2021-5-20  

+ 代码优化
  - [代码优化细节](note/optimization/代码优化细节.md)  最后更新于2021-5-12

### 框架
+ Spring
  - [知识点](note/spring/知识点.md)  最后更新于2021-2-4  
  - [spring使用技巧](note/spring/spring使用技巧.md)  最后更新于2021-3-14  
  
+ Spring Security
  - [问题合集](note/security/问题合集.md)  最后更新于2021-1-19  
  - [知识点](note/security/知识点.md)  最后更新于2021-1-19  

+ 微服务
  - 分布式事务  
    - [知识点](note/microservice/transaction/知识点.md)  最后更新于2020-12-25   
    - [Seata](note/microservice/transaction/Seata.md)  最后更新于2020-12-25  
    - [Seata分布式事务TA模式源码解读](note/microservice/transaction/Seata分布式事务TA模式源码解读.md)  最后更新于2021-5-29  
  - 远程调用
    - [Feign](note/microservice/rpc/Feign.md)  最后更新于2021-2-19  
  - 服务发现
    - [浅谈Kubernetes中的服务发现](note/microservice/discovery/浅谈Kubernetes中的服务发现.md)  最后更新于2021-5-13  
  

+ 中间件
  - 消息队列
    - [知识点](note/middleware/mq/知识点.md)  最后更新于2020-12-28  
    - [面试角度学习Kafka](note/middleware/mq/面试角度学习Kafka.md)  最后更新于2021-4-28  

+ 开箱即用类
  - [定时器-Quartz](note/other-frame/quartz.md)  最后更新于2020-12-29 暂时弃坑  

### 数据库
+ MySQL
  - [问题合集](note/mysql/问题合集.md)  最后更新于2020-12-13  
  - [知识点](note/mysql/知识点.md)  最后更新于2021-2-3  
  - [面试题](note/mysql/面试题.md)  最后更新于2021-1-30  
  - [InnoDB解决幻读的方案--LBCC&MVCC](note/mysql/transaction/InnoDB解决幻读的方案--LBCC&MVCC.md)  最后更新于2021-6-1  

+ Redis
  - [知识点](note/redis/知识点.md)  最后更新于2020-12-15  
  - [Redis分布式锁五种方案](note/redis/Redis分布式锁五种方案.md)  最后更新于2021-6-7  
  
### 网络编程
+ 计算机网络
  - [图解网络-摸清网络的第一步](note/net/diagram/图解网络-摸清网络的第一步.md)  最后更新于2021-5-30  
  - [图解网络-一个数据包在网络中的心路历程](note/net/diagram/图解网络-一个数据包在网络中的心路历程.md)  最后更新于2021-5-31  
  - [图解网络-ping的工作原理](note/net/diagram/图解网络-ping的工作原理.md)  最后更新于2021-5-31  

+ I/O
  - [知识点](note/io/知识点.md)  最后更新于2020-12-23
  - NIO系列
    - [TPC](note/io/nio/NIO系列-TCP.md)  最后更新于2021-4-3  
  - netty  
    - [Netty模型架构](note/io/netty/Netty模型架构.md)  最后更新于2021-5-27  
    - [Netty基础-Reactor模型](note/io/netty/Netty基础-Reactor模型.md)  最后更新于2021-5-27  
    - [Netty基础-Netty线程模型](note/io/netty/Netty基础-Netty线程模型.md)  最后更新于2021-5-27  
    - [Netty基础-Netty核心api](note/io/netty/Netty基础-Netty核心api.md)  最后更新于2021-5-28  
    - [Netty基础-Netty异步模型](note/io/netty/Netty基础-Netty异步模型.md)  最后更新于2021-5-28  

+ HTTP
  - [知识点](note/http/知识点.md)  最后更新于2020-12-23  
  - [HTTP1.1](note/http/HTPP1.1.md)  最后更新于2021-4-3 
  - [HTTP2](note/http/HTTP2.md)  最后更新于2021-4-3  
  - [30张图解HTTP常见的面试题](note/http/30张图解HTTP常见的面试题.md)  最后更新于2021-5-29  
  - [优化HTTP1.1](note/http/优化HTTP1.1.md)  最后更新于2021-6-2  

+ WebSocket
  - [知识点](note/websocket/知识点.md)  最后更新于2020-12-28  

### 计算机组成
+ 图解计算机
  - [图解计算机-CPU执行程序过程](note/computer/diagram/CPU执行程序过程.md)  最后更新于2021-6-2  
  - [图解计算机-存储器的层次结构](note/computer/diagram/存储器的层次结构.md)  最后更新于2021-6-3  

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

### 领域驱动设计-DDD
+ [DDD-面向对象思想](note/ddd/DDD-面向对象思想.md)  最后更新于2021-4-4  