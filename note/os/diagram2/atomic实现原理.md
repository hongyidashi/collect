# atomic实现原理

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [零、开篇](#%E9%9B%B6%E5%BC%80%E7%AF%87)
- [一、问题背景](#%E4%B8%80%E9%97%AE%E9%A2%98%E8%83%8C%E6%99%AF)
- [一、Bus Lock](#%E4%B8%80bus-lock)
- [二、Cacheline Lock](#%E4%BA%8Ccacheline-lock)
- [三、LL/SC](#%E4%B8%89llsc)
- [四、总结](#%E5%9B%9B%E6%80%BB%E7%BB%93)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

[TOC]

## 零、开篇

atomic是原子的意思，意味"不可分割"的整体。在Linux
kernel中有一类atomic操作API。这些操作对用户而言是原子执行的，在一个CPU上执行过程中，不会被其他CPU打断。最常见的操作是原子读改写，简称RMW。例如，atomic_inc()
接口。atomic硬件实现和Cache到底有什么关系呢？其实有一点关系，下面会一步步揭晓答案。

## 一、问题背景

我们先来看看不使用原子操作的时候，我们会遇到什么问题。我们知道increase一个变量，CPU微观指令级别分成3步操作。

1. 先read变量的值到CPU内存寄存器；
2. 对寄存器的值递增；
3. 将寄存器的值写回变量。

例如不使用原子指令的情况下在多个CPU上执行以下increase函数。

```go
int counter = 0;

void increase(void){
    counter++;
}
```

例如2个CPU得系统，初始值counter为0。在两个CPU上同时执行以上increase函数。可能出现如下操作序列：

```text
   +    +----------------------+----------------------+
   |    |    CPU0 operation    |    CPU1 operation    |
   |    +----------------------+----------------------+
   |    | read counter (== 0)  |                      |
   |    +----------------------+----------------------+
   |    |       increase       | read counter (== 0)  |
   |    +----------------------+----------------------+
   |    | write counter (== 1) |       increase       |
   |    +----------------------+----------------------+
   |    |                      | write counter (== 1) |
   |    +----------------------+----------------------+
   V
timeline
```

我们可以清晰地看到，当CPU0读取counter的值位0后，在执行increase操作的同时，CPU1也读取counter变量，同样counter的值依然是0。随后CPU0和CPU1先后将1的值写入内存。实际上，我们想执行两次increase操作，我应该得到counter值为2。但是实际上得到的是1。这不是我们想要的结果。为了解决这个问题，硬件引入原子自增指令。保证CPU0递增原子变量counter之间，不被其他CPU执行自增指令导致不想要的结果。硬件是如何实现原子操作期间不被打断呢？

## 一、Bus Lock

当CPU发出一个原子操作时，可以先锁住Bus（总线）。这样就可以防止其他CPU的内存操作。等原子操作结束，释放Bus。这样后续的内存操作就可以进行。这个方法可以实现原子操作，但是锁住Bus会导致后续无关内存操作都不能继续。实际上，我们只关心我们操作的地址数据。只要我们操作的地址锁住即可，而其他无关的地址数据访问依然可以继续。所以我们引入另一种解决方法。

## 二、Cacheline Lock

为了实现多核Cache一致性，现在的硬件基本采用MESI协议（或者MESI变种）维护一致性。因此我们可以借助多核Cache一致性协议MESI实现原子操作。我们知道Cache
line的状态处于Exclusive或者Modified时，可以说明该变量只有当前CPU私有Cache缓存了该数据。所以我们可以直接修改Cache
line即可更新数据。并且MESI协议可以帮我们保证互斥。当然这不能不能保证RMW操作期间不被打断，因此我们还需要做些手脚实现原子操作。

我们依然假设只有2个CPU的系统。当CPU0试图执行原子递增操作时。

1. CPU0发出"Read Invalidate"消息，其他CPU将原子变量所在的缓存无效，并从Cache返回数据。CPU0将Cache line置成Exclusive状态。然后将该**cache line标记locked**。
2. 然后CPU0读取原子变量，修改，最后写入cache line。
3. 将cache line置位unlocked。

在步骤1和3之间，如果其他CPU（例如CPU1）尝试执行一个原子递增操作，CPU1会发送一个"Read Invalidate"消息，CPU0收到消息后，检查对应的cache
line的状态是locked，暂时不回复消息（CPU1会一直等待CPU0回复Invalidate Acknowledge消息）。直到cache line变成unlocked。这样就可以实现原子操作。我们称这种方式为锁cache
line。这种实现方式必须要求操作的变量位于一个cache line。

## 三、LL/SC

LL/SC(Load-Link/Store-Conditional)是另一种硬件实现方法。例如aarch64架构就采用这种方法。这种方法就不是我们关注的重点了。略过。

## 四、总结

借助多核Cache一致性协议可以很方便实现原子操作。当然远不止上面举例说的atomic_inc还有很多其他类似的原子操作，例如原子比较交换等。

