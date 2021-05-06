# JVM深入-Synchronized锁升级

[TOC]

## 一、无锁

 jvm会有4秒的偏向锁开启的延迟时间，在这个偏向延迟内对象处于为无锁态。如果关闭偏向锁启动延迟、或是经过4秒且没有线程竞争对象的锁，那么对象会进入**无锁可偏向**状态。

准确来说，无锁可偏向状态应该叫做**匿名偏向**(`Anonymously biased`)状态，因为这时对象的`mark word`中后三位已经是`101`，但是`threadId`指针部分仍然全部为0，它还没有向任何线程偏向。综上所述，对象在刚被创建时，根据jvm的配置对象可能会处于 **无锁** 或 **匿名偏向** 两个状态。

此外，如果在jvm的参数中关闭偏向锁，那么直到有线程获取这个锁对象之前，会一直处于无锁不可偏向状态。修改jvm启动参数：

```
-XX:-UseBiasedLocking
```

延迟5s后打印对象内存布局：

```java
public static void main(String[] args) throws InterruptedException {
    User user = new User();
    TimeUnit.SECONDS.sleep(5);
    System.out.println(ClassLayout.parseInstance(user).toPrintable());
}
```

运行结果：

![png](images/无锁不偏向状态结果.png)

可以看到，即使经过一定的启动延时，对象一直处于`001`无锁不可偏向状态。大家可能会有疑问，在无锁状态下，为什么要存在一个不可偏向状态呢？别人的解释是：

> JVM内部的代码有很多地方也用到了synchronized，明确在这些地方存在线程的竞争，如果还需要从偏向状态再逐步升级，会带来额外的性能损耗，所以JVM设置了一个偏向锁的启动延迟，来降低性能损耗

也就是说，在无锁不可偏向状态下，如果有线程试图获取锁，那么将跳过升级偏向锁的过程，直接使用轻量级锁。使用代码进行验证：

```java
public static void main(String[] args) throws InterruptedException {
    User user=new User();
    synchronized (user){
        System.out.println(ClassLayout.parseInstance(user).toPrintable());
    }
}
```

查看结果可以看到，在关闭偏向锁情况下使用`synchronized`，锁会直接升级为轻量级锁（`00`状态）:

![png](images/无锁不偏向升级到轻量级锁.png)

可关闭偏向锁延迟`-XX:BiasedLockingStartupDelay=0`并开启偏向锁自行验证，会发现结果是`101`。

在目前的基础上，可以用流程图概括上面的过程：

![png](images/无锁偏向锁轻量级锁升级流程.png)

额外注意一点就是匿名偏向状态下，如果调用系统的`hashCode()`方法，会使对象回到无锁态，并在`markword`中写入`hashCode`。并且在这个状态下，如果有线程尝试获取锁，会直接从无锁升级到轻量级锁，不会再升级为偏向锁。

