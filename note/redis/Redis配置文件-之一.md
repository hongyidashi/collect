# Redis配置文件-之一

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [零、开篇](#%E9%9B%B6%E5%BC%80%E7%AF%87)
- [一、UNITS](#%E4%B8%80units)
- [二、INCLUDES](#%E4%BA%8Cincludes)
- [三、MODULES](#%E4%B8%89modules)
- [四、NETWORK](#%E5%9B%9Bnetwork)
- [五、GENERAL](#%E4%BA%94general)
    - [1. daemonize](#1-daemonize)
    - [2. supervised no](#2-supervised-no)
    - [3. pidfile](#3-pidfile)
    - [4. loglevel notice](#4-loglevel-notice)
    - [5. logfile](#5-logfile)
    - [6. database](#6-database)
    - [7. syslog](#7-syslog)
- [六、 Security （安全）](#%E5%85%AD-security-%E5%AE%89%E5%85%A8)
- [七、CLIENTS](#%E4%B8%83clients)
- [八、MEMORY MANAGEMENT](#%E5%85%ABmemory-management)
    - [1. 最大缓存](#1-%E6%9C%80%E5%A4%A7%E7%BC%93%E5%AD%98)
    - [2.  最大缓存策略](#2--%E6%9C%80%E5%A4%A7%E7%BC%93%E5%AD%98%E7%AD%96%E7%95%A5)
    - [3. 样本数量](#3-%E6%A0%B7%E6%9C%AC%E6%95%B0%E9%87%8F)
    - [4. 副本忽略最大内存](#4-%E5%89%AF%E6%9C%AC%E5%BF%BD%E7%95%A5%E6%9C%80%E5%A4%A7%E5%86%85%E5%AD%98)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

[TOC]

# 零、开篇

本文`Redis`的版本为`5.0.7`

## 一、UNITS

```1k => 1000 bytes
1kb => 1024 bytes
1m => 1000000 bytes
1mb => 1024*1024 bytes
1g => 1000000000 bytes
1gb => 1024*1024*1024 bytes
```

> 单位不区分大小写，只支持bytes

## 二、INCLUDES

和`structs2`配置文件类似，可以通过`includes`包含。`redis.conf`可以作为总闸，包含其他。

```
include /path/to/local.conf
include /path/to/other.conf
```

## 三、MODULES

```
loadmodule /path/to/my_module.so
loadmodule /path/to/other_module.so
```

`Redis`可以通过`loadmodule`选项在启动时加载模块，若服务端无法加载模块，服务端会停止。可以通过多个`loadmodule`选项加载多个模块。

## 四、NETWORK

- **「bind 127.0.0.1」**：默认情况下，如果未指定“bind”配置指令，`Redis`将侦听服务器上所有可用网络接口的连接。

可以使用“bind”配置指令，后跟一个或多个IP地址，只侦听一个或多个选定接口。**「例如」**：bind 192.168.1.100 10.0.0.1

当设置多个`bind`地址后，`Redis`内部会维护多个`Socket`，每个`Socket`用于一个`network interface`。

- **「protected-mode yes」**：此选项默认开启。

当`Redis`服务端未使用`bind`选项显式指定要监听的`network interface`，并且未设置密码，`Redis`服务端只会接受来自`127.0.0.1`和`::1`的客户端以及`Unix`域的`Socket`进行连接。

- **「port 6379」**：用于设置`Redis`监听的`TCP`端口，默认为6379，设置为0表示不监听`TCP`端口
- **「timeout 0」**：空闲多少秒之后关闭连接，“0”表示不关闭
- **「tcp-keepalive 300」**：单位为秒，如果为0，则不会进行`keepalive`检测，建议设置成60
- **「tcp-backlog 511」**：设置`tcp`的`backlog`，`backlog`其实是一个连接队列。

> `backlog`队列总和 = 未完成三次握手队列 + 已经完成三次握手队列

在高并发环境下需要一个高`backlog`值来避免慢客户端连接问题。

**「注意」**：`Linux`内核会将这个值减小到`/proc/sys/net/core/somaxconn`的值，所以需要确认增大`somaxconn`和`tcp_max_syn_backlog`两个值来达到想要的效果。

## 五、GENERAL

### 1. daemonize

`Redis`采用的是单进程多线程的模式，`daemonize`是用来指定`redis`是否要用守护线程的方式启动。默认情况下，`Redis`不作为守护进程运行。如果需要，请使用“是”。

```
#daemonize no 
//当前界面将进入redis的命令行界面，
exit强制退出或者关闭连接工具(putty，
xshell等)都会导致redis进程退出。

daemonize yes     
//代表开启守护进程模式。在该模式下，
redis 会在后台运行，并将进程 pid 号写入
至 redis.conf 选项 pidfile 设置的文件中，
此时 redis 将一直运行，除非手动kill该进程。
```

### 2. supervised no

当你通过`upstart`或者`systemd`运行`Redis`时，`Redis`可以和你的`supervision tree`进行交互，可选的选项为：

- no 无交互（默认）
- upstart 通过向`Redis`发送`SIGSTOP`信号来通知`upstart`
- systemd 通过向`$NOTIFY_SOCKET`写入`READY=1`来通知`systemd`
- auto 通过是否设置了`UPSTART_JOB`或者`NOTIFY_SOCKET`环境变量来决定选项为 `upstart`或者`systemd`

### 3. pidfile

```
pidfile /var/run/redis_6379.pid //进程pid文件
```

### 4. loglevel notice

指定服务器日志级别：从上到下依次减少

- `debug`：大量信息，对开发/测试有用
- `verbose`：许多很少有用的信息，但不像调试级别那样混乱
- `notice`：适度冗长，可能是生产中需要的内容
- `warning`：只记录非常重要/关键的消息

### 5. logfile

```
logfile ""
```

日志的名字，如果为空，`redis`给控制台标准输出，如果配置为守护进程方式运行，且设置了`logfile`为`stdout`，则日志将会发送给`/dev/null`

### 6. database

```
databases 16
```

系统默认的库16个，默认使用0库

### 7. syslog

- `syslog-enabled no`：是否把日志输出到`syslog`中，系统日志默认是关着
- `syslog-ident redis`：指定`syslog`里的日志标志设备以`redis`开头
- `syslog-facility local0`：指定`syslog`设备，值可以是`USER`或`LOCAL0-LOCAL7`，默认使用`local0`

## 六、 Security （安全）

```
requirepass 12345!@#
```

设置`redis`连接密码，如果配置了连接密码，客户端在连接`redis`时需要通过`Auth <password>`命令提供密码，默认关闭。

如果设置完密码，`ping`就失败了，提示“NoAuth Authentication required”，加上`auth + 密码`就通了。

**「要求必须auth + password 在任何命令之前」**

> `Redis`一般做的是缓存，不是安全，而且系统会认为`Linux`是在安全的环境下。

## 七、CLIENTS

`maxclients 10000`：最大连接数

设置`redis`同时可以与多少个客户端进行连接。默认情况下为`10000`个客户端。

当你无法设置进程文件句柄限制时，`redis`会设置为当前的文件句柄限制值减去`32`，因为`redis`会为自身内部处理逻辑留一些句柄出来。

如果达到了此限制，`redis`则会拒绝新的连接请求，并且向这些连接请求方发出**「max number of clients reached」**以作回应。

## 八、MEMORY MANAGEMENT

设置`redis`可以使用的内存量。一旦到达内存使用上限，`redis`将会试图移除内部数据，移除规则可以通过`maxmemory-policy`来指定。

如果`redis`无法根据移除规则来移除内存中的数据，或者设置了**「不允许移除」**，那么`redis`则会针对那些需要申请内存的指令返回错误信息，比如`SET`、`LPUSH`等。但是对于无内存申请的指令，仍然会正常响应，比如`GET`等。

如果你的`redis`是主`redis`（说明你的`redis`有从`redis`），那么在设置内存使用上限时，需要在系统中留出一些内存空间给同步队列缓存，只有在你设置的是“不移除”的情况下，才不用考虑这个因素。

### 1. 最大缓存

```
#maxmemory <bytes>
maxmemory 128MB
```

设置`maxmemory`和相对应的回收策略算法，设置最好为物理内存的**「3/4」**，或者比例更小，因为`redis`复制数据等其他服务时，也是需要缓存的。以防缓存数据过大致使`redis`崩溃，造成系统出错不可用。

牺牲一部分缓存数据，保存整体系统可用性。`redis`新的内存机制，会把`key`放在内存，`value`存放在`swap`区。

此配置需要和**「maxmemory-policy」**配合使用，当`redis`中内存数据达到`maxmemory`时，触发**「清除策略」**。在**「内存不足」**时，任何`write`操作(比如`set`，`lpush`等)都会触发**「清除策略」**的执行。

**实际环境**

建议`redis`的所有物理机器的硬件配置保持一致(内存一致)，同时确保`master/replica`中**「maxmemory policy」**配置一致。

**内存满时**

如果还接收到`set`命令，`redis`将先尝试剔除设置过`expire`信息的`key`，而不管该`key`的过期时间有没有到达。

在删除时，将按照过期时间进行删除，最早将要被过期的`key`将最先被删除。如果带有`expire`信息的`key`都删光了，内存还不够用，那么将返回错误。这样，`redis`将不再接收写请求，只接收`get`请求。

> `maxmemory`的设置比较适合于把`redis`当作于类似`memcached`的缓存来使用。

### 2.  最大缓存策略

**「maxmemory-policy」**:

- volatile-lru：使用`LRU`（最近最少使用）算法移除`key`，只对设置了过期时间的键
- allkeys-lru：使用`LRU`算法移除`key`（所有`key`）
- volatile-lfu：对过期键使用 LFU（最不经常使用）近似算法
- allkeys-lfu：对所有键使用 LFU 近似算法
- volatile-random：在过期集合中移除随机的`key`，只对设置了过期时间的键
- allkeys-random：移除随机的`key`
- volatile-ttl：移除那些`TTL`值最小的`key`，即那些最近要过期的`key`
- noeviction：不进行移除。针对写操作，只是返回错误信息（默认）（去公司观察维度，不应该选择这个）

> `LRU`算法、`LFU`算法或者`TTL`算法都是不是很精确算法，而是个近似算法。

**「使用策略规则：」**

1. 如果数据呈现幂律分布，也就是一部分数据访问频率高，一部分数据访问频率低，则使用`allkeys-lru`。
2. 如果数据呈现平等分布，也就是所有的数据访问频率都相同，则使用`allkeys-random`。

### 3. 样本数量

设置样本数量，上边提到的算法都并非是精确的算法，而是估算值，所以你可以设置样本的大小。

```
maxmemory-samples 5
```

默认值是 5，也就是说`Redis`随机挑出5个键，然后选出一个最符合条件的。对`LRU`来说5是比较合适的。10已经很接近于真正的`LRU`，但会消耗更多的`CPU`。3会更快但没有那么精确。

### 4. 副本忽略最大内存

```
replica-ignore-maxmemory yes
```

从`Redis 5`开始，默认情况下，`replica`节点会忽略`maxmemory`设置（除非在发生`failover`后，此节点被提升为`master`节点）。

这意味着只有`master`才会执行过期删除策略，并且`master`在删除键之后会对`replica`发送`DEL`命令。

这个行为保证了`master`和`replicas`的一致性，并且这通常也是你需要的，但是若你的`replica`节点是可写的，或者你希望`replica`节点有不同的内存配置，并且你确保所有到`replica`写操作都幂等的，那么你可以修改这个默认的行为 （请确保你明白你在做什么）。

**「注意」**默认情况下`replica`节点不会执行过期策略，它有可能使用了超过`maxmemory`设定的值的内存。因此你需要监控`replicas`节点所在的机器并且确保在`master`节点到达配置的`maxmemory`大小时，`replicas`节点不会超过物理内存的大小。


今天我们就先说到这了，至于配置文件中关于主从复制和持久化部分我们将在后续的内容进行讲解。