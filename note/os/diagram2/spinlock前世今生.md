# spinlock前世今生

[TOC]

## 零、开篇

高速缓存的基本原理算是介绍差不多了。软件一般都会极致的配合硬件，尽最大努力优化性能。软件围绕着高速缓存做出的优化就有很多。我们以一个点作为切入，探究spinlock的前世今生。spinlock是Linux kernel中常见的互斥原语，适用于不可睡眠场景。可以说是最基础的同步原语。不知你是否研究过spinlock的进化史？从最初的wild spinlock到ticket spinlock，再到今天的qspinlock，可谓是一波三折，大起大落啊！spinlock究竟是怎么一步一步发生蜕变，又是为什么发生变化。背后究竟是什么原因推动着自身的变革呢？我一直认为Linux kernel源码是一部历史，git log就是其自传。从git log中我们可以对其历史窥探一二，但终究是管中窥豹。然而，这并不能阻止我们去了解她的步伐，我依然怀着敬畏之心去了解其历史。历史可以帮助我们更好的理解藏在代码背后的故事和原理。我们以高速缓存为主题，围绕着spinlock的前世今生，尝试探索spinlock和Cache之间的恩怨情仇。

## 一、wild spinlock

首先介绍下这位主角，spinlock是互斥原语，用于不可睡眠上下文环境访问共享数据的互斥。同一时间只有一个进程（当然说法不够严谨，也可以是softirq，hardirq等）可以获得锁，其他不能获得spinlock的进程原地自旋，直到获取锁。如果让你实现spinlock，是不是觉得很简单。

```go
struct spinlock {
        int locked;
};


void spin_lock(struct spinlock *lock)
{
        while (lock->locked);
        lock->locked = 1;
}


void spin_unlock(struct spinlock *lock)
{
        lock->locked = 0;
}
```

看起来是不是很简单。spinlock的locked成员是1代表locked。如果是0代表锁是释放状态。spin_lock()保证在不能获得锁的情况下一直spin等待。不过这里有个小问题需要修改下，spin_lock()中判断locked是0的同时需要将locked置1必须保证是原子操作。所以我们需要一条原子操作。我们假设test_and_set()就是一条原子操作，测试一个变量的值，然后置为1。返回值是变量的旧值。这一系列操作在硬件实现是原子的。

```go
void spin_lock(struct spinlock *lock)
{
        while (test_and_set(&lock->locked));
}
```

我们假设系统有8个CPU。假设8个CPU同时申请spinlock，CPU0成功持有锁。想一下CPU1-CPU7在做什么？test_and_set是无条件置1操作。所以CPU1-CPU7一直在写变量。根据多核Cache一致性协议MESI，我们知道修改一个变量，必须是这个变量在cache line中的状态是E或者M。所以，CPU1写变量时会通过MESI协议发送invalid消息给其他CPU。然后修改变量为1。CPU1修改变量之前，会发送invalid消息给CPU0，然后修改变量。CPU2修改变量前发送invalid消息给CPU1，然后修改变量。CPU3发送invalid消息给CPU2，然后修改变量。CPU4-7也在重复这些事情。这就无形之中增加了带宽压力，使性能下降。变量在CPU1-CPU7的cache中来回颠簸。我们既要spin又想避免cache颠簸导致的带宽压力和性能损失。我们知道cache line有一种状态是shared，可以在多个CPU之间共享，而不用发送invalid消息，而且CPU1-CPU7一直在spin，修改变量也没有意义，只有CPU0 unlock的时候，修改才有价值。所以我们对spin_lock()修改如下。

```go
void spin_lock(struct spinlock *lock)
{
        while (lock->locked || test_and_set(&lock->locked));
}
```

这样CPU1-CPU7会一直保持Shared状态，没有cache颠簸。这种优化在Linux kernel代码中经常可见，例如[这里](https://link.zhihu.com/?target=https%3A//elixir.bootlin.com/linux/v5.4.33/source/include/linux/bit_spinlock.h%23L16)，当你看到这种奇怪的写法时，不要奇怪罢了。当spin_unlock()时，这种Shared状态会被打破。但是这也足以一定程度上提升了性能。好景并没有持续很长时间，我们发现了新的问题。某些等待的CPU可能会有饥饿现象。例如，CPU1-CPU7在原地spin，CPU0释放锁的时候，CPU1-CPU7哪个CPU的cache先看到locked的值，就会先获得锁。所以不排队的机制可能导致部分CPU饿死。为了解决这个问题，我们从wild spinlock跨度到ticket spinlock。

## 二、ticket spinlock

历史又更近了一步，我们引入排队机制，以FIFO的顺序处理申请者。谁先申请，谁先获得。保证公平性。

```go
struct spinlock {
        unsigned short owner;
        unsigned short next;
};


void spin_lock(struct spinlock *lock)
{
        unsigned short next = xadd(&lock->next, 1);
        while (lock->owner != next);
}


void spin_unlock(struct spinlock *lock)
{
        lock->owner++;
}
```

spin_lock()中xadd()也是一条原子操作，原子的将变量加1，并返回变量之前的值。在这一版实现中，我们可以确定当owner等于next时，代表锁是释放状态。否则，说明锁是持有状态。next就像是排队拿票机制，每来一个申请者，next就加1，代表票号。owner代表当前持锁的票号。看起来一切都完美了，但是现实往往是残酷的。接下来又遇到什么问题了呢？

## 三、ticket spinlock的问题

我们依然以上面的8个CPU的系统为例说明。CPU0-CPU7依次申请spinlock。假设初始状态，spinlock变量的结构体没有缓存到任何CPU的高速缓存。CPU0获取spinlock，此时的cache状态如下。

![png](images/8-cache状态1.png)

然后CPU1申请锁，并更新spinlock变量。所以会invalid CPU0的cache。然后spinlock变量缓存在CPU1的cache，然后spin一直读取spinlock的值。

![png](images/8-cache状态2.png)

接着CPU2申请锁，并更新spinlock变量。所以会invalid CPU1的cache。然后更新next的值。而CPU1又会读取spinlock的值，所以spinlock变量对应的cache line的状态最终是Shared。并且同时缓存在CPU1和CPU2的cache。

![png](images/8-cache状态3.png)

后面CPU3-CPU7依次申请spinlock，就是重复上面的操作。更新next的前，invalid其他CPU的cache，然后其他CPU在从当前CPU获取更新后的spinlock值。cache line状态更新Shared，然后继续spin。

![png](images/8-cache状态4.png)

当CPU0 spin_unlcok()的时候，先invalid CPU1-CPU7对应的cache line。然后更新owner的值。

![png](images/8-cache状态5.png)

CPU1-CPU7读取owner的值又会从CPU0获取，缓存到各自cache中。

![png](images/8-cache状态6.png)

不知道你是否发现了问题，随着CPU数量的增多，总线带宽压力很大。而且延迟也会随着增长，性能也会逐渐下降。而且CPU0释放锁后，CPU1-CPU7也只有一个CPU可以获得锁，理论上没有必要影响其他CPU的缓存，只需要影响接下来应该获取锁的CPU（按照FIFO的顺序）。这说明ticket spinlock不是scalable（同样最初的wild spinlock也存在此问题）。所以历史又往前迈了一个脚步。

## 四、qspinlock

我们来到了qspinlock的时代，qspinlock的出现就是为了解决tickeet spinlock的上述问题。我先来思考下造成该问题的原因。根因就是每个CPU都spin在共享变量spinlock上。所以我们只需要保证每个CPU spin的变量是不同的就可以避免这种情况了。所以我们需要换一种排队的方式。例如单链表。单链表也可以做到FIFO，每次解锁时，也只需要通知链表头的CPU即可。这其实就是MCS锁的实现原理。qspinlock的实现是建立在MCS锁的理论基础上。我们先探究下MCS锁是如何实现。

```go
struct mcs_spinlock {
        struct mcs_spinlock *next;
        int locked;
};
```

mcs_spinlock中next成员就是构建单链表的基础，所有spin自旋的CPU都可以借助mcs_spinlock结构体构建单链表关系。mcs_spinlock结构体需要几个呢？我们知道spin_lock()期间，抢占是关闭的，所以最多只可能存在CPU数量减1个CPU自旋等待，所以我们只需要CPU个数的mcs_spinlock结构体（spinlock可能会出现嵌套，例如softirq可以中断进程上下文，hardirq可以中断softirq，NMI可以中断hardirq。这意味着一个CPU可能出现4个不同的spinlock自旋等待，所以理论上来说mcs_spinlock结构体需要4*CPU数量。后面为了简化问题不考虑单核spinlock嵌套情况），这就很适合使用percpu变量。spin等锁的操作只需要将所属自己CPU的mcs_spinlock结构体加入单链表尾部，然后spin，直到自己的mcs_spinlock的locked成员置1（locked初始值是0）。unlock的操作也很简单，只需要将解锁的CPU对应的mcs_spinlock结构体的next域的lock成员置1，相当于通知下一个CPU退出循环。

我们继续以上面的8个CPU的系统为例说明。首先CPU0申请spinlock时，发现链表是空，并且锁是释放状态。所以CPU0获得锁。

![png](images/8-qspinlock1.png)

CPU1继续申请spinlock，需要spin等待。所以将CPU1对应的mcs_spinlock结构体加入单链表尾部。然后spin等待CPU1对应的mcs_spinlock结构体locked成员被置1。

![png](images/8-qspinlock2.png)

当CPU2继续申请锁时，发现链表不为空，说明有CPU在等待锁。所以也将CPU2对应的mcs_spinlock结构体加入链表尾部。

![png](images/8-qspinlock3.png)

当CPU0释放锁的时候，发现CPU0对应的mcs_spinlock结构体的next域不为NULL，说明有等待的CPU。然后将next域指向的mcs_spinlock结构体的locked成员置1，通知下个获得锁的CPU退出自旋。MCS lock头指针可以选择不更新，等到CPU2释放锁时更新为NULL。

![png](images/8-qspinlock4.png)

这里只是个简单的参考，MCS锁具体代码细节可参考[这里](https://link.zhihu.com/?target=https%3A//elixir.bootlin.com/linux/v5.4.33/source/kernel/locking/mcs_spinlock.h)。如何将mcs_spinlock头指针塞进spinlock 4个字节的身躯，成为实现qspinlock的关键。不过这部分可以参考linux kernel源码，掌握了其原理，看代码实现应该易如反掌。通过以上步骤，我们可以看到每个CPU都spin在自己的使用变量上面。因此不会存在ticket spinlock的问题。

## 五、总结

从wild spinlock到qspinlock，代码复杂度增加的不是一点半点，而是很多。qspinlock的实现文件足足500行往上，而wild spinlock仅仅几十行。如今Linux kernel的代码让我们不知所措的很大原因可能就是软件不断迭代，不断的优化，不断的bug fix，使代码变得非常复杂。然而我并又不是跟随着Linux一路走来的人，我并不了解她的历史。也不知道她怎么从一个孩童长成如今的巨人。毕竟她比我出生还早好几年。而我第一眼面对就是这庞大的身躯，如何去了解她，深入她，成为了关键。而我认为最好的方式就是了解她历史，她怎么一步步成长为今天的模样。我们应该能从中学到很多。

