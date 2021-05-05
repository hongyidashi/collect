# JVM深入-Java对象内存布局

[TOC]

> 文中代码基于 JDK 1.8.0_261，64-Bit HotSpot 运行



## 一、对象内存结构概述

在介绍对象在内存中的组成结构前，我们先简要回顾一个对象的创建过程：

1. jvm将对象所在的`class`文件加载到方法区中

2. jvm读取`main`方法入口，将`main`方法入栈，执行创建对象代码

3. 在`main`方法的栈内存中分配对象的引用，在堆中分配内存放入创建的对象，并将栈中的引用指向堆中的对象

所以当对象在实例化完成之后，是被存放在堆内存中的，这里的对象由3部分组成，如下图所示：

![png](images/java对象内存示意图.png)

对各个组成部分的功能简要进行说明：

- 对象头：对象头存储的是对象在运行时状态的相关信息、指向该对象所属类的元数据的指针，如果对象是数组对象那么还会额外存储对象的数组长度
- 实例数据：实例数据存储的是对象的真正有效数据，也就是各个属性字段的值，如果在拥有父类的情况下，还会包含父类的字段。字段的存储顺序会受到数据类型长度、以及虚拟机的分配策略的影响
- 对齐填充字节：在java对象中，需要对齐填充字节的原因是，64位的jvm中对象的大小被要求向8字节对齐，因此当对象的长度不足8字节的整数倍时，需要在对象中进行填充操作。注意图中对齐填充部分使用了虚线，这是因为填充字节并不是固定存在的部分，这点在后面计算对象大小时具体进行说明

## 二、JOL 工具简介

在具体开始研究对象的内存结构之前，先介绍一下我们要用到的工具，`openjdk`官网提供了查看对象内存布局的工具`jol (java object layout)`，可在`maven`中引入坐标：

```xml
<dependency>
    <groupId>org.openjdk.jol</groupId>
    <artifactId>jol-core</artifactId>
    <version>0.14</version>
    <scope>provided</scope>
</dependency>
```

在代码中使用`jol`提供的方法查看jvm信息：

```java
System.out.println(VM.current().details());
```

![png](images/jol查看JVM信息截图.png)

通过打印出来的信息，可以看到我们使用的是64位 jvm，并开启了指针压缩，对象默认使用8字节对齐方式。通过`jol`查看对象内存布局的方法，将在后面的例子中具体展示，下面开始对象内存布局的正式学习。

## 三、对象头

首先看一下对象头（`Object header`）的组成部分，根据普通对象和数组对象的不同，结构将会有所不同。只有当对象是数组对象才会有数组长度部分，普通对象没有该部分，如下图所示：

![png](images/java对象头结构示意图.png)

在对象头中`mark word` 占8字节，默认开启指针压缩的情况下`klass pointer` 占4字节，数组对象的数组长度占4字节。在了解了对象头的基础结构后，现在以一个不包含任何属性的空对象为例，查看一下它的内存布局，创建`User`类：

```java
public class User {
}
```

使用`jol`查看对象头的内存布局：

```java
public class HeaderDemo {
    public static void main(String[] args) {
        User user = new User();
        //查看对象的内存布局
        System.out.println(ClassLayout.parseInstance(user).toPrintable());
    }
}
```

执行代码，查看打印信息：

![png](images/jol查看user对象内存布局.png)

- `OFFSET`：偏移地址，单位为字节
- `SIZE`：占用内存大小，单位为字节
- `TYPE`：`Class`中定义的类型
- `DESCRIPTION`：类型描述，`Obejct header` 表示对象头，`alignment`表示对齐填充
- `VALUE`：对应内存中存储的值

当前对象共占用16字节，因为8字节标记字加4字节的类型指针，不满足向8字节对齐，因此需要填充4个字节：

```
8B (mark word) + 4B (klass pointer) + 0B (instance data) + 4B (padding)
```

### 3.1 Mark Word 标记字

在对象头中，`mark word` 一共有64个bit，用于存储对象自身的运行时数据，标记对象处于以下5种状态中的某一种：

![png](images/markword值列表.png)

#### 3.1.1 基于mark word的锁升级

在jdk6 之前，通过`synchronized`关键字加锁时使用无差别的的重量级锁，重量级锁会造成线程的串行执行，并且使cpu在用户态和核心态之间频繁切换。随着对`synchronized`的不断优化，提出了锁升级的概念，并引入了偏向锁、轻量级锁、重量级锁。在`mark word`中，锁（`lock`）标志位占用2个bit，结合1个bit偏向锁（`biased_lock`）标志位，这样通过**倒数的3位**，就能用来标识当前对象持有的锁的状态，并判断出其余位存储的是什么信息。

基于`mark word`的锁升级的流程如下：

1. 锁对象刚创建时，没有任何线程竞争，对象处于无锁状态。在上面打印的空对象的内存布局中，根据大小端，得到最后8位是`00000001`，表示处于无锁态，并且处于不可偏向状态。这是因为在jdk中偏向锁存在延迟4秒启动，也就是说在jvm启动后4秒后创建的对象才会开启偏向锁，我们通过jvm参数取消这个延迟时间：

```
-XX:BiasedLockingStartupDelay=0
```

可以通过指定JVM运行参数是可以取消延迟，也可以采用sleep的方式看到结果：

![png](images/header-偏向锁结果图.png)

> 后续栗子采用指定JVM参数的方式取消延迟

这时最后3位为`101`，表示当前对象的锁没有被持有，并且处于可被偏向状态。

2. 在没有线程竞争的条件下，第一个获取锁的线程通过`CAS`将自己的`threadId`写入到该对象的`mark word`中，若后续该线程再次获取锁，需要比较当前线程`threadId`和对象`mark word`中的`threadId`是否一致，如果一致那么可以直接获取，并且锁对象始终保持对该线程的偏向，也就是说偏向锁不会主动释放。

使用代码进行测试同一个线程重复获取锁的过程：

```java
public static void main(String[] args) throws InterruptedException {
    User user=new User();
    synchronized (user){
      System.out.println(ClassLayout.parseInstance(user).toPrintable());
    }
    System.out.println(ClassLayout.parseInstance(user).toPrintable());
    synchronized (user){
      System.out.println(ClassLayout.parseInstance(user).toPrintable());
    }
}
```

执行结果：

![png](images/偏向锁运行示意图.png)

可以看到一个线程对一个对象加锁、解锁、重新获取对象的锁时，`mark word`都没有发生变化，偏向锁中的当前线程指针始终指向同一个线程。

3. 当两个或以上线程交替获取锁，但并没有在对象上并发的获取锁时，偏向锁升级为轻量级锁。在此阶段，线程采取`CAS`的自旋方式尝试获取锁，避免阻塞线程造成的cpu在用户态和内核态间转换的消耗。测试代码如下：

```java
public static void main(String[] args) throws InterruptedException {
    User user = new User();
    synchronized (user) {
      System.out.println("--MAIN--:" + ClassLayout.parseInstance(user).toPrintable());
    }

    Thread thread = new Thread(() -> {
      synchronized (user) {
        System.out.println("--THREAD--:" + ClassLayout.parseInstance(user).toPrintable());
      }
    });
    thread.start();
    thread.join();
    System.out.println("--END--:" + ClassLayout.parseInstance(user).toPrintable());
}
```

运行结果：

![png](images/偏向锁出现失效被打破.png)

整个加锁状态的变化流程如下：

- 主线程首先对user对象加锁，首次加锁为`101`偏向锁
- 子线程等待主线程释放锁后，对user对象加锁，这时将偏向锁升级为`00`轻量级锁
- 轻量级锁解锁后，user对象无线程竞争，恢复为`001`无锁态，并且处于不可偏向状态。如果之后有线程再尝试获取user对象的锁，会直接加轻量级锁，而不是偏向锁

4. 当两个或以上线程并发的在同一个对象上进行同步时，为了避免无用自旋消耗cpu，轻量级锁会升级成重量级锁。这时`mark word`中的指针指向的是`monitor`对象（也被称为管程或监视器锁）的起始地址。测试代码如下：

```java
public static void main(String[] args) throws InterruptedException {
    User user = new User();
    new Thread(() -> {
      synchronized (user) {
        System.out.println("--THREAD1--:" + ClassLayout.parseInstance(user).toPrintable());
        try {
          TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
    new Thread(() -> {
      synchronized (user) {
        System.out.println("--THREAD2--:" + ClassLayout.parseInstance(user).toPrintable());
        try {
          TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
}
```

运行结果：

![png](images/升级为重量级锁header.png)

可以看到，在两个线程同时竞争user对象的锁时，会升级为`10`重量级锁。

#### 3.1.2 其他信息

对`mark word` 中其他重要信息进行说明：

- `hashcode`：无锁态下的`hashcode`采用了延迟加载技术，在第一次调用`hashCode()`方法时才会计算写入。对这一过程进行验证：

```java
public static void main(String[] args) throws InterruptedException {
    User user=new User();
    //打印内存布局
    System.out.println(ClassLayout.parseInstance(user).toPrintable());
    //计算hashCode
    System.out.println(user.hashCode());
    //再次打印内存布局
    System.out.println(ClassLayout.parseInstance(user).toPrintable());
}
```

运行结果：

![png](images/验证hashcode延迟加载.png)

可以看到，在没有调用`hashCode()`方法前，31位的哈希值不存在，全部填充为0。在调用方法后，根据大小端，被填充的数据为：

```
10100010000101101011110110101
```

将2进制转换为10进制，对应哈希值`339924917`。需要注意，只有在调用没有被重写的`Object.hashCode()`方法或`System.identityHashCode(Object)`方法才会写入`mark word`，执行用户自定义的`hashCode()`方法不会被写入。

> 如要验证，切记这个hashcode是倒着组装的，如图顺序可组装出10100010000101101011110110101
>
> 烦死了

大家可能会注意到，当对象被加锁后，`mark word`中就没有足够空间来保存`hashCode`了，这时`hashcode`会被移动到重量级锁的`Object Monitor`中。

- `epoch`：偏向锁的时间戳

- 分代年龄（`age`）：在`jvm`的垃圾回收过程中，每当对象经过一次`Young GC`，年龄都会加1，这里4位来表示分代年龄最大值为15，这也就是为什么对象的年龄超过15后会被移到老年代的原因。在启动时可以通过添加参数来改变年龄阈值：

```
-XX:MaxTenuringThreshold
```

当设置的阈值超过15时，启动时会报错：

![png](images/分代年龄设置错误.png)

