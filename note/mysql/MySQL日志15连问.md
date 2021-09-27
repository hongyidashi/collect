# MySQL日志15连问

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [1. redo log是什么? 为什么需要redo log？](#1-redo-log%E6%98%AF%E4%BB%80%E4%B9%88-%E4%B8%BA%E4%BB%80%E4%B9%88%E9%9C%80%E8%A6%81redo-log)
- [2. 什么是WAL技术, 好处是什么?](#2-%E4%BB%80%E4%B9%88%E6%98%AFwal%E6%8A%80%E6%9C%AF-%E5%A5%BD%E5%A4%84%E6%98%AF%E4%BB%80%E4%B9%88)
- [3. redo log的写入方式](#3-redo-log%E7%9A%84%E5%86%99%E5%85%A5%E6%96%B9%E5%BC%8F)
- [4. Redo log的执行流程](#4-redo-log%E7%9A%84%E6%89%A7%E8%A1%8C%E6%B5%81%E7%A8%8B)
- [5. redo log 为什么可以保证crash safe机制呢？](#5-redo-log-%E4%B8%BA%E4%BB%80%E4%B9%88%E5%8F%AF%E4%BB%A5%E4%BF%9D%E8%AF%81crash-safe%E6%9C%BA%E5%88%B6%E5%91%A2)
- [6. binlog的概念是什么, 起到什么作用, 可以保证crash-safe吗?](#6-binlog%E7%9A%84%E6%A6%82%E5%BF%B5%E6%98%AF%E4%BB%80%E4%B9%88-%E8%B5%B7%E5%88%B0%E4%BB%80%E4%B9%88%E4%BD%9C%E7%94%A8-%E5%8F%AF%E4%BB%A5%E4%BF%9D%E8%AF%81crash-safe%E5%90%97)
- [7. binlog和redolog的不同点有哪些?](#7-binlog%E5%92%8Credolog%E7%9A%84%E4%B8%8D%E5%90%8C%E7%82%B9%E6%9C%89%E5%93%AA%E4%BA%9B)
- [8. 执行器和innoDB在执行update语句时候的流程是什么样的?](#8-%E6%89%A7%E8%A1%8C%E5%99%A8%E5%92%8Cinnodb%E5%9C%A8%E6%89%A7%E8%A1%8Cupdate%E8%AF%AD%E5%8F%A5%E6%97%B6%E5%80%99%E7%9A%84%E6%B5%81%E7%A8%8B%E6%98%AF%E4%BB%80%E4%B9%88%E6%A0%B7%E7%9A%84)
- [9. 如果数据库误操作, 如何执行数据恢复?](#9-%E5%A6%82%E6%9E%9C%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AF%AF%E6%93%8D%E4%BD%9C-%E5%A6%82%E4%BD%95%E6%89%A7%E8%A1%8C%E6%95%B0%E6%8D%AE%E6%81%A2%E5%A4%8D)
- [10. binlog日志三种格式](#10-binlog%E6%97%A5%E5%BF%97%E4%B8%89%E7%A7%8D%E6%A0%BC%E5%BC%8F)
- [11. 什么是MySQL两阶段提交, 为什么需要两阶段提交?](#11-%E4%BB%80%E4%B9%88%E6%98%AFmysql%E4%B8%A4%E9%98%B6%E6%AE%B5%E6%8F%90%E4%BA%A4-%E4%B8%BA%E4%BB%80%E4%B9%88%E9%9C%80%E8%A6%81%E4%B8%A4%E9%98%B6%E6%AE%B5%E6%8F%90%E4%BA%A4)
- [12. 如果不是两阶段提交, 先写redo log和先写bin log两种情况各会遇到什么问题?](#12-%E5%A6%82%E6%9E%9C%E4%B8%8D%E6%98%AF%E4%B8%A4%E9%98%B6%E6%AE%B5%E6%8F%90%E4%BA%A4-%E5%85%88%E5%86%99redo-log%E5%92%8C%E5%85%88%E5%86%99bin-log%E4%B8%A4%E7%A7%8D%E6%83%85%E5%86%B5%E5%90%84%E4%BC%9A%E9%81%87%E5%88%B0%E4%BB%80%E4%B9%88%E9%97%AE%E9%A2%98)
- [13. binlog刷盘机制](#13-binlog%E5%88%B7%E7%9B%98%E6%9C%BA%E5%88%B6)
- [14.undo log 是什么？它有什么用](#14undo-log-%E6%98%AF%E4%BB%80%E4%B9%88%E5%AE%83%E6%9C%89%E4%BB%80%E4%B9%88%E7%94%A8)
- [15. 说说Redo log的记录方式](#15-%E8%AF%B4%E8%AF%B4redo-log%E7%9A%84%E8%AE%B0%E5%BD%95%E6%96%B9%E5%BC%8F)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

[TOC]

## 1. redo log是什么? 为什么需要redo log？

**redo log 是什么呢**?

- redo log 是**重做日志**。
- 它记录了**数据页**上的改动。
- 它指**事务**中修改了的数据，将会备份存储。
- 发生数据库服务器宕机、或者脏页未写入磁盘，可以通过redo log恢复。
- 它是**Innodb存储**引擎独有的

**为什么需要 redo log？**

- redo log主要用于MySQL异常重启后的一种数据恢复手段，确保了数据的一致性。
- 其实是为了配合MySQL的WAL机制。因为MySQL进行更新操作，为了能够快速响应，所以采用了异步写回磁盘的技术，写入内存后就返回。但是这样，会存在**crash后**内存数据丢失的隐患，而redo log具备crash
  safe的能力。

## 2. 什么是WAL技术, 好处是什么?

- WAL，全称是Write-Ahead Logging(可以翻译为 日志先行)，它的关键点就是日志先写内存，再写磁盘。MySQL执行更新操作后，**在真正把数据写入到磁盘前，先记录日志**。
- 好处是不用每一次操作都实时把数据写盘，就算crash后也可以通过redo log恢复，所以能够实现快速响应SQL语句。

## 3. redo log的写入方式

redo log包括两部分内容，分别是内存中的**日志缓冲**(redo log buffer)和磁盘上的**日志文件**(redo log file)。

mysql每执行一条DML语句，会先把记录写入**redo log buffer**，后续某个时间点再一次性将多个操作记录写到**redo log file**。这种先写日志，再写磁盘的技术，就是**WAL**。

在计算机操作系统中，用户空间(user space)下的缓冲区数据，一般是无法直接写入磁盘的，必须经过操作系统内核空间缓冲区(即OS Buffer)。

- 日志最开始会写入位于存储引擎Innodb的redo log buffer，这个是在用户空间完成的。
- 然后再将日志保存到操作系统内核空间的缓冲区(OS buffer)中。
- 最后，通过系统调用`fsync()`，从**OS buffer**写入到磁盘上的**redo log file**中，完成写入操作。这个写入磁盘的操作，就叫做**刷盘**。

![png](images/mysql15-redolog写入方式.png)

我们可以发现，redo log buffer写入到redo log file，是经过OS buffer中转的。其实可以通过参数`innodb_flush_log_at_trx_commit`进行配置，参数值含义如下：

- 0：称为**延迟写**事务提交时不会将redo log buffer中日志写入到OS buffer，而是每秒写入OS buffer并调用写入到redo log file中。
- 1：称为**实时写，实时刷**。事务每次提交都会将redo log buffer中的日志写入OS buffer并保存到redo log file中。
- 2：称为**实时写，延迟刷**。每次事务提交写入到OS buffer，然后是每秒将日志写入到redo log file。

## 4. Redo log的执行流程

我们来看下Redo log的执行流程，假设执行的SQL如下：

```sql
update T
set a =1
where id = 666
```

![png](images/mysql15-redolog执行流程.png)

1. MySQL客户端将请求语句`update T set a =1 where id =666`，发往MySQL Server层。
2. MySQL Server 层接收到SQL请求后，对其进行分析、优化、执行等处理工作，将生成的SQL执行计划发到InnoDb存储引擎层执行。
3. InnoDb存储引擎层将**a修改为1**的这个操作记录到内存中。
4. 记录到内存以后会修改redo log 的记录，会在添加一行记录，其内容是**需要在哪个数据页上做什么修改**。
5. 此后，将事务的状态设置为prepare ，说明已经准备好提交事务了。
6. 等到MySQL Server层处理完事务以后，会将事务的状态设置为**commit**，也就是提交该事务。
7. 在收到事务提交的请求以后，**redo log**会把刚才写入内存中的操作记录写入到磁盘中，从而完成整个日志的记录过程。

## 5. redo log 为什么可以保证crash safe机制呢？

- 因为redo log每次更新操作完成后，就一定会写入的，如果**写入失败**，说明此次操作失败，事务也不可能提交。
- redo log内部结构是基于页的，记录了这个页的字段值变化，只要crash后读取redo log进行重放，就可以恢复数据。

## 6. binlog的概念是什么, 起到什么作用, 可以保证crash-safe吗?

- bin log是归档日志，属于MySQL Server层的日志。可以实现**主从复制**和**数据恢复**两个作用。
- 当需要**恢复数据**时，可以取出某个时间范围内的bin log进行重放恢复。
- 但是bin log不可以做crash safe，因为crash之前，bin log**可能没有写入完全**MySQL就挂了。所以需要配合**redo log**才可以进行crash safe。

## 7. binlog和redolog的不同点有哪些?

![png](images/mysql15-binlog和redolog区别.png)

## 8. 执行器和innoDB在执行update语句时候的流程是什么样的?

- 执行器在优化器选择了索引后，会调用InnoDB读接口，读取要更新的行到内存中。
- 执行SQL操作后，更新到内存，然后写redo log，写bin log，此时即为完成。
- 后续InnoDB会在合适的时候把此次操作的结果写回到磁盘。

## 9. 如果数据库误操作, 如何执行数据恢复?

![png](images/mysql15-跑路.png)

数据库在某个时候误操作，就可以找到距离误操作最近的时间节点的bin log，重放到临时数据库里，然后选择误删的数据节点，恢复到线上数据库。

## 10. binlog日志三种格式

binlog日志有三种格式

- Statement：基于SQL语句的复制(statement-based replication,SBR)
- Row:基于行的复制。(row-based replication,RBR)
- Mixed:混合模式复制。(mixed-based replication,MBR)

**Statement格式**

每一条会修改数据的sql都会记录在binlog中

- 优点：不需要记录每一行的变化，减少了binlog日志量，节约了IO，提高性能。
- 缺点：由于记录的只是执行语句，为了这些语句能在备库上正确运行，还必须记录每条语句在执行的时候的一些相关信息，以保证所有语句能在备库得到和在主库端执行时候相同的结果。

**Row格式**

不记录sql语句上下文相关信息，仅保存哪条记录被修改。

- 优点：binlog中可以不记录执行的sql语句的上下文相关的信息，仅需要记录那一条记录被修改成什么了。所以row
  level的日志内容会非常清楚的记录下每一行数据修改的细节。不会出现某些特定情况下的存储过程、或function、或trigger(~~特利迦,不是~~)的调用和触发无法被正确复制的问题。
- 缺点:可能会产生大量的日志内容。

**Mixed格式**

Statement与Row的结合。一般的语句修改使用statment格式保存binlog，如一些函数，statement无法完成主从复制的操作，则采用row格式保存binlog,MySQL会根据执行的每一条具体的sql语句来区分对待记录的日志形式。

## 11. 什么是MySQL两阶段提交, 为什么需要两阶段提交?

其实所谓的两阶段就是把一个事务分成两个阶段来提交。

![png](images/mysql15-两阶段提交.png)

两阶段提交主要有三步曲：

1. redo log在写入后，进入prepare状态
2. 执行器写入bin log
3. 进入commit状态，事务可以提交。

**为什么需要两阶段提交呢?**

- 如果不用两阶段提交的话，可能会出现这样情况：bin log写入之前，机器crash导致需要重启，重启后redo log继续重放crash之前的操作，而当bin log后续需要作为备份恢复时，会出现数据不一致的情况。
- 如果是bin log commit之前crash，那么重启后，发现redo log是prepare状态且bin log完整（bin log写入成功后，redo log会有bin log的标记），就会自动commit，让存储引擎提交事务。
- 两阶段提交就是为了保证redo log和binlog数据的安全一致性。只有在这两个日志文件逻辑上高度一致了。你才能放心的使用redo
  log帮你将数据库中的状态恢复成crash之前的状态，使用binlog实现数据备份、恢复、以及主从复制。

## 12. 如果不是两阶段提交, 先写redo log和先写bin log两种情况各会遇到什么问题?

- 先写redo log，crash后bin log备份恢复时少了一次更新，与当前数据不一致。
- 先写bin log，crash后，由于redo log没写入，事务无效，所以后续bin log备份恢复时，数据不一致。

## 13. binlog刷盘机制

所有未提交的事务产生的binlog，都会被先记录到binlog的缓存中。等该事务提交时，再将缓存中的数据写入binlog日志文件中。缓存的大小由参数`binlog_chache_size`控制。

binlog什么时候刷新到磁盘呢？由参数`sync_binlog`控制

- 当`sync_binlog`为0时，表示MySQL不控制binlog的刷新，而是由系统自行判断何时写入磁盘。选这种策略，一旦操作系统宕机，缓存中的binlog就会丢失。
- `sync_binlog`为N时，每N个事务，才会将binlog写入磁盘。
- 当`sync_binlog`为1时，则表示每次commit，都将binlog 写入磁盘。

来看一个比较完整的流程图吧：

![png](images/mysql15-binlog刷盘机制.png)

## 14.undo log 是什么？它有什么用

- undo log 叫做回滚日志，用于记录数据被修改前的信息。
- 它跟redo log重做日志所记录的相反，重做日志记录数据被修改后的信息。undo log主要记录的是数据的逻辑变化，为了在发生错误时回滚之前的操作，需要将之前的操作都记录下来，这样发生错误时才可以回滚。

## 15. 说说Redo log的记录方式

redo log的大小是固定。它采用循环写的方式记录，当写到结尾时，会回到开头循环写日志。如下图（图片来源网络）：

![png](images/mysql15-redolog循环写入.png)

redo log buffer(内存中)是由首尾相连的四个文件组成的，它们分别是：ib_logfile_1、ib_logfile_2、ib_logfile_3、ib_logfile_4。

> - write pos表示当前写入记录位置(写入磁盘的数据页的逻辑序列位置)
> - check point表示刷盘(写入磁盘)后对应的位置。
> - write pos到check point之间的部分用来记录新日志，也就是留给新记录的空间。
> - check point到write pos之间是待刷盘的记录，如果不刷盘会被新记录覆盖。

有了 redo log，当数据库发生宕机重启后，可通过 redo log将未落盘的数据（check point之后的数据）恢复，保证已经提交的事务记录不会丢失，这种能力称为**crash-safe**。