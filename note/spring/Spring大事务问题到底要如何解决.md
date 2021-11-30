# Spring大事务问题到底要如何解决

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [一、大事务引发的问题](#%E4%B8%80%E5%A4%A7%E4%BA%8B%E5%8A%A1%E5%BC%95%E5%8F%91%E7%9A%84%E9%97%AE%E9%A2%98)
- [二、解决办法](#%E4%BA%8C%E8%A7%A3%E5%86%B3%E5%8A%9E%E6%B3%95)
    - [1. 少用@Transactional注解](#1-%E5%B0%91%E7%94%A8transactional%E6%B3%A8%E8%A7%A3)
    - [2. 将查询(select)方法放到事务外](#2-%E5%B0%86%E6%9F%A5%E8%AF%A2select%E6%96%B9%E6%B3%95%E6%94%BE%E5%88%B0%E4%BA%8B%E5%8A%A1%E5%A4%96)
    - [3. 事务中避免远程调用](#3-%E4%BA%8B%E5%8A%A1%E4%B8%AD%E9%81%BF%E5%85%8D%E8%BF%9C%E7%A8%8B%E8%B0%83%E7%94%A8)
    - [4. 事务中避免一次性处理太多数据](#4-%E4%BA%8B%E5%8A%A1%E4%B8%AD%E9%81%BF%E5%85%8D%E4%B8%80%E6%AC%A1%E6%80%A7%E5%A4%84%E7%90%86%E5%A4%AA%E5%A4%9A%E6%95%B0%E6%8D%AE)
    - [5. 非事务执行](#5-%E9%9D%9E%E4%BA%8B%E5%8A%A1%E6%89%A7%E8%A1%8C)
    - [6. 异步处理](#6-%E5%BC%82%E6%AD%A5%E5%A4%84%E7%90%86)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

[TOC]

## 一、大事务引发的问题

在分享解决办法之前，先看看系统中如果出现大事务可能会引发哪些问题

![png](images/1-大事务引发的问题.png)

从上图可以看出如果系统中出现大事务时，问题还不小，所以我们在实际项目开发中应该尽量避免大事务的情况。如果我们已有系统中存在大事务问题，该如何解决呢？

## 二、解决办法

### 1. 少用@Transactional注解

大家在实际项目开发中，我们在业务方法加上`@Transactional`注解开启事务功能，这是非常普遍的做法，它被称为`声明式事务`。

部分代码如下：

```java
@Transactional(rollbackFor = Exception.class)
public void save(User user){
        doSameThing...
        }
```

然而，我要说的第一条是：少用`@Transactional`注解。

为什么？

1. 我们知道`@Transactional`注解是通过`spring`的`aop`起作用的，但是如果使用不当，事务功能可能会失效。如果恰巧你经验不足，这种问题不太好排查。
2. `@Transactional`注解一般加在某个业务方法上，会导致整个业务方法都在同一个事务中，粒度太粗，不好控制事务范围，是出现大事务问题的最常见的原因。

那我们该怎么办呢？

可以使用`编程式事务`，在`spring`项目中使用`TransactionTemplate`类的对象，手动执行事务。

部分代码如下：

```java
@Autowired
private TransactionTemplate transactionTemplate;

        ...

public void save(final User user){
        transactionTemplate.execute((status)=>{
        doSameThing...
        return Boolean.TRUE;
        })
        }
```

从上面的代码中可以看出，使用`TransactionTemplate`的`编程式事务`功能自己灵活控制事务的范围，是避免大事务问题的首选办法。

当然，我说少使用`@Transactional`注解开启事务，并不是说一定不能用它，如果项目中有些业务逻辑比较简单，而且不经常变动，使用`@Transactional`
注解开启事务开启事务也无妨，因为它更简单，开发效率更高，但是千万要小心事务失效的问题。

### 2. 将查询(select)方法放到事务外

如果出现大事务，可以将查询(select)方法放到事务外，也是比较常用的做法，因为一般情况下这类方法是不需要事务的。

比如出现如下代码：

```java
@Transactional(rollbackFor = Exception.class)
public void save(User user){
        queryData1();
        queryData2();
        addData1();
        updateData2();
        }
```

可以将`queryData1`和`queryData2`两个查询方法放在事务外执行，将真正需要事务执行的代码才放到事务中，比如：`addData1`和`updateData2`方法，这样就能有效的减少事务的粒度。

如果使用`TransactionTemplate`的`编程式事务`这里就非常好修改。

```java
@Autowired
private TransactionTemplate transactionTemplate;

        ...

public void save(final User user){
        queryData1();
        queryData2();
        transactionTemplate.execute((status)=>{
        addData1();
        updateData2();
        return Boolean.TRUE;
        })
        }
```

但是如果你实在还是想用`@Transactional`注解，该怎么拆分呢？

```java
public void save(User user){
        queryData1();
        queryData2();
        doSave();
        }

@Transactional(rollbackFor = Exception.class)
public void doSave(User user){
        addData1();
        updateData2();
        }
```

这个例子是非常经典的错误，这种直接方法调用的做法事务不会生效，给正在坑中的朋友提个醒。因为`@Transactional`注解的声明式事务是通过`spring aop`起作用的，而`spring aop`
需要生成代理对象，直接方法调用使用的还是原始对象，所以事务不会生效。

有没有办法解决这个问题呢？

1. 新加一个Service方法

这个方法非常简单，只需要新加一个Service方法，把`@Transactional`注解加到新Service方法上，把需要事务执行的代码移到新方法中。具体代码如下：

```java
@Servcie
publicclass ServiceA{
@Autowired
  prvate ServiceB serviceB;

public void save(User user){
        queryData1();
        queryData2();
        serviceB.doSave(user);
        }
        }

@Servcie
publicclass ServiceB{

@Transactional(rollbackFor = Exception.class)
public void doSave(User user){
        addData1();
        updateData2();
        }

        }
```

2. 在该Service类中注入自己

如果不想再新加一个Service类，在该Service类中注入自己也是一种选择。具体代码如下：

```java
@Servcie
publicclass ServiceA{
@Autowired
  prvate ServiceA serviceA;

public void save(User user){
        queryData1();
        queryData2();
        serviceA.doSave(user);
        }

@Transactional(rollbackFor = Exception.class)
public void doSave(User user){
        addData1();
        updateData2();
        }
        }
```

可能有些人可能会有这样的疑问：这种做法会不会出现循环依赖问题？

其实`spring ioc`内部的三级缓存保证了它，不会出现循环依赖问题。

3. 在该Service类中使用AopContext.currentProxy()获取代理对象

上面的方法2确实可以解决问题，但是代码看起来并不直观，还可以通过在该Service类中使用AOPProxy获取代理对象，实现相同的功能。具体代码如下：

```java
@Servcie
publicclass ServiceA{

public void save(User user){
        queryData1();
        queryData2();
        ((ServiceA)AopContext.currentProxy()).doSave(user);
        }

@Transactional(rollbackFor = Exception.class)
public void doSave(User user){
        addData1();
        updateData2();
        }
        }
```

### 3. 事务中避免远程调用

我们在接口中调用其他系统的接口是不能避免的，由于网络不稳定，这种远程调的响应时间可能比较长，如果远程调用的代码放在某个事物中，这个事物就可能是大事务。当然，远程调用不仅仅是指调用接口，还有包括：发MQ消息，或者连接redis、mongodb保存数据等。

```java
@Transactional(rollbackFor = Exception.class)
public void save(User user){
        callRemoteApi();
        addData1();
        }
```

远程调用的代码可能耗时较长，切记一定要放在事务之外。

```java
@Autowired
private TransactionTemplate transactionTemplate;

        ...

public void save(final User user){
        callRemoteApi();
        transactionTemplate.execute((status)=>{
        addData1();
        return Boolean.TRUE;
        })
        }
```

有些朋友可能会问，远程调用的代码不放在事务中如何保证数据一致性呢？这就需要建立：`重试`+`补偿机制`，达到数据`最终一致性`了。

### 4. 事务中避免一次性处理太多数据

如果一个事务中需要处理的数据太多，也会造成大事务问题。比如为了操作方便，你可能会一次批量更新1000条数据，这样会导致大量数据锁等待，特别在高并发的系统中问题尤为明显。

解决办法是分页处理，1000条数据，分50页，一次只处理20条数据，这样可以大大减少大事务的出现。

### 5. 非事务执行

在使用事务之前，我们都应该思考一下，是不是所有的数据库操作都需要在事务中执行？

```java
@Autowired
private TransactionTemplate transactionTemplate;

        ...

public void save(final User user){
        transactionTemplate.execute((status)=>{
        addData();
        addLog();
        updateCount();
        return Boolean.TRUE;
        })
        }
```

上面的例子中，其实`addLog`增加操作日志方法 和 `updateCount`更新统计数量方法，是可以不在事务中执行的，因为操作日志和统计数量这种业务允许少量数据不一致的情况。

```java
@Autowired
private TransactionTemplate transactionTemplate;

        ...

public void save(final User user){
        transactionTemplate.execute((status)=>{
        addData();
        return Boolean.TRUE;
        })
        addLog();
        updateCount();
        }
```

当然大事务中要鉴别出哪些方法可以非事务执行，其实没那么容易，需要对整个业务梳理一遍，才能找出最合理的答案。

### 6. 异步处理

还有一点也非常重要，是不是事务中的所有方法都需要同步执行？我们都知道，方法同步执行需要等待方法返回，如果一个事务中同步执行的方法太多了，势必会造成等待时间过长，出现大事务问题。

看看下面这个列子：

```java
@Autowired
private TransactionTemplate transactionTemplate;

        ...

public void save(final User user){
        transactionTemplate.execute((status)=>{
        order();
        delivery();
        return Boolean.TRUE;
        })
        }
```

`order`方法用于下单，`delivery`方法用于发货，是不是下单后就一定要马上发货呢？

答案是否定的。

这里发货功能其实可以走mq异步处理逻辑。

```java
@Autowired
private TransactionTemplate transactionTemplate;

        ...

public void save(final User user){
        transactionTemplate.execute((status)=>{
        order();
        return Boolean.TRUE;
        })
        sendMq();
        }
```