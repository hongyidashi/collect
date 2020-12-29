# Quartz的使用

目录
+ [基本概念](#基本概念)
+ [实战](#实战)


## 基本概念
Quartz是OpenSymphony开源组织在Job scheduling领域又一个开源项目，它可以与J2EE与J2SE应用程序相结合也可以单独使用。在java企业级应用中，Quartz是使用最广泛的定时调度框架。

在Quartz中的主要概念：
- Scheduler：调度任务的主要API
- ScheduleBuilder：用于构建Scheduler，例如其简单实现类SimpleScheduleBuilder
- Job：调度任务执行的接口，也即定时任务执行的方法
- JobDetail：定时任务作业的实例
- JobBuilder：关联具体的Job，用于构建JobDetail
- Trigger：定义调度执行计划的组件，即定时执行
- TriggerBuilder：构建Trigger

## 实战

