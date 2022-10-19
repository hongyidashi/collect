# 多核Cache一致性

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [零、开篇](#%E9%9B%B6%E5%BC%80%E7%AF%87)
- [一、问题背景](#%E4%B8%80%E9%97%AE%E9%A2%98%E8%83%8C%E6%99%AF)
- [二、Bus Snooping Protocol](#%E4%BA%8Cbus-snooping-protocol)
- [三、MESI Protocol](#%E4%B8%89mesi-protocol)
- [四、MESI Protocol Messages](#%E5%9B%9Bmesi-protocol-messages)
- [五、总结](#%E4%BA%94%E6%80%BB%E7%BB%93)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

[TOC]

## 零、开篇

经过这么多篇文章的介绍，我们应该已经对Cache有一个比较清晰的认识。Cache会面临哪些问题，我们该怎么处理这些问题。现在我们讨论多核Cache一致性问题。在摩尔定律不太适用的今天，人们试图增加CPU核数以提升系统整体性能。这类系统称之为多核系统（简称MP，Multi-Processor）。我们知道每个CPU都有一个私有的L1
Cache（不细分iCache和dCache）。假设一个2核的系统，我们将会有2个L1 Cache。这就引入了一个问题，不同CPU之间的L1 Cache如何保证一致性呢？首先看下什么是多核Cache一致性问题。

## 一、问题背景

首先我们假设2个CPU的系统，并且L1 Cache的cache line大小是64 Bytes。两个CPU都读取0x40地址数据，导致0x40开始的64 Bytes内容分别加载到CPU0和CPU1的私有的cache line。

![png](images/7-双核L1cache.png)

CPU0执行写操作，写入值0x01。CPU0私有的L1 Cache更新cache
line的值。然后，CPU1读取0x40数据，CPU1发现命中cache，然后返回0x00值，并不是CPU0写入的0x01。这就造成了CPU0和CPU1私有L1 Cache数据不一致现象。

![png](images/7-双核L1cache不一致.png)

按照正确的处理流程，我们应该需要以下方法保证多核Cache一致性：

- CPU0修改0x40的时候，除了更新CPU0的Cache之外，还应该通知CPU1的Cache更新0x40的数据。
- CPU0修改0x40的时候，除了更新CPU0的Cache之外，还可以通知CPU1的Cache将0x40地址所在cache
  line置成invalid。保证CPU1读取数据时不会命中自己的Cache。不命中自己的cache之后，我们有两种选择保证读取到最新的数据：
    - 从CPU0的私有cache中返回0x40的数据给CPU1；
    - CPU0发出invalid信号后，将写入0x40的数据写回主存，CPU1从主存读取最新的数据。

以上问题就是一个简单的不一致性现象。我们需要保证多核一致性，就需要办法维护一致性。可以有2种方法维护一致性，分别是软件和硬件。软件维护一致性的方法，现在基本没有采用。因为软件维护成本太高，由于维护一致性带来的性能损失抵消一部分cache带来的性能提升。所以现在的硬件会帮我们维护多核Cache一致性，并且对软件是透明的。感兴趣的朋友可以继续往下了解硬件是如何维护多核Cache一致性。

## 二、Bus Snooping Protocol

继续以上面的例子说明bus
snooping的工作机制。当CPU0修改自己私有的Cache时，硬件就会广播通知到总线上其他所有的CPU。对于每个CPU来说会有特殊的硬件监听广播事件，并检查是否有相同的数据被缓存在自己的CPU，这里是指CPU1。如果CPU1私有Cache已经缓存即将修改的数据，那么CPU1的私有Cache也需要更新对应的cache
line。这个过程就称作bus snooping。如下图所示，我们只考虑L1 dCache之间的一致性。

![png](images/7-Bus-Snooping.png)

这种bus
snooping方法简单，但要需要每时每刻监听总线上的一切活动。我们需要明白的一个问题是不管别的CPU私有Cache是否缓存相同的数据，都需要发出一次广播事件。这在一定程度上加重了总线负载，也增加了读写延迟。针对该问题，提出了一种状态机机制降低带宽压力。这就是MESI
protocol（协议）。

## 三、MESI Protocol

MESI是现在一种使用广泛的协议，用来维护多核Cache一致性。我们可以将MESI看做是状态机。我们将每一个cache line标记状态，并且维护状态的切换。cache
line的状态可以像tag，modify等类似存储。继续以上面的例子说明问题。

1. 当CPU0读取0x40数据，数据被缓存到CPU0私有Cache，此时CPU1没有缓存0x40数据，所以我们标记cache line状态为Exclusive。Exclusive代表cache
   line对应的数据仅在数据只在一个CPU的私有Cache中缓存，并且其在缓存中的内容与主存的内容一致。
2. 然后CPU1读取0x40数据，发送消息给其他CPU，发现数据被缓存到CPU0私有Cache，数据从CPU0 Cache返回给CPU1。此时CPU0和CPU1同时缓存0x40数据，此时cache
   line状态从Exclusive切换到Shared状态。Shared代表cache line对应的数据在"多"个CPU私有Cache中被缓存，并且其在缓存中的内容与主存的内容一致。
3. 继续CPU0修改0x40地址数据，发现0x40内容所在cache line状态是Shared。CPU0发出invalid消息传递到其他CPU，这里是CPU1。CPU1接收到invalid消息。将0x40所在的cache
   line置为Invalid状态。Invalid状态表示表明当前cache line无效。然后CPU0收到CPU1已经invalid的消息，修改0x40所在的cache line中数据。并更新cache
   line状态为Modified。Modified表明cache line对应的数据仅在一个CPU私有Cache中被缓存，并且其在缓存中的内容与主存的内容不一致，代表数据被修改。
4. 如果CPU0继续修改0x40数据，此时发现其对应的cache line的状态是Modified。因此CPU0不需要向其他CPU发送消息，直接更新数据即可。
5. 如果0x40所在的cache line需要替换，发现cache line状态是Modified。所以数据应该先写回主存。

以上是cache line状态改变的举例。我们可以知道cache line具有4中状态，分别是Modified、Exclusive、Shared和Invalid。取其首字母简称MESI。当cache
line状态是Modified或者Exclusive状态时，修改其数据不需要发送消息给其他CPU，这在一定程度上减轻了带宽压力。

## 四、MESI Protocol Messages

Cache之间数据和状态同步沟通，是通过发送message同步和沟通。MESI主要涉及一下几种message。

- Read: 如果CPU需要读取某个地址的数据。
- Read Response: 答复一个读消息，并且返回需要读取的数据。
- Invalidate: 请求其他CPU invalid地址对应的cache line。
- Invalidate Acknowledge: 回复invalidate消息，表明对应的cache line已经被invalidate。
- Read Invalidate: Read + Invalidate消息的组合。
- Writeback: 该消息包含要回写到内存的地址和数据。

继续以上的例子，我们有5个步骤。现在加上这些message，看看消息是怎么传递的。

1. CPU0发出Read消息。主存返回Read Response消息，消息包含地址0x40的数据。
2. CPU1发出Read消息，CPU0返回Read Response消息，消息包含地址0x40数据。
3. CPU0发出Invalidate消息，CPU1接到消息后，返回Invalidate Acknowledge消息。
4. 不需要发送任何消息。
5. 发送Writeback消息。

## 五、总结

多核Cache一致性由硬件保证，对软件来说是透明的。因此我们不用再考虑多核Cache一致性问题。另外，现在CPU硬件采用的一致性协议一般是MESI的变种。例如ARM64架构采用的MOESI
Protocol。多一种Owned状态。多出来的状态也是为了更好的优化性能。

