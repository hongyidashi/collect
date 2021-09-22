# Disruptor-入门

[TOC]

## 一、disruptor简介

disruptor是LMAX公司开发的一个高性能队列，其作用和阻塞队列(BlockingQueue)类似，都是在相同进程内、不同线程间传递数据(例如消息、事件)，另外disruptor也有自己的一些特色：

1. 以广播的形式发布事件，并且消费者之间存在依赖关系；
2. 为事件提前分配内存；
3. 无锁算法；

### 关于Ring Buffer(环形队列)
提到disruptor一般都会提到Ring Buffer(环形队列)是它的特点，实际上从3.0版本之后，环形队列只是用来存储和更新事件数据，在其他更复杂的场景下，用户可以通过自定义操作将其替换掉；

## 二、用disruptor实现消息的发布和消费的套路

咱们提前小结用disruptor实现消息的发布和消费的套路，后面的开发按部就班即可，括号中是本篇对应的java类：
1. 事件的定义：一个普通的bean（StringEvent.java）
2. 事件工厂：定义如何生产事件的内存实例，这个实例刚从内存中创建，还没有任何业务数据（StringEventFactory.java）
3. 事件处理：封装了消费单个事件的具体逻辑（StringEventHandler.java）
4. 事件生产者：定义了如何将业务数据设置到还没有业务数据的事件中，就是工厂创建出来的那种（StringEventProducer.java）
5. 初始化逻辑：创建和启动disruptor对象，将事件工厂传给disruptor，创建事件生产者和事件处理对象，并分别与disruptor对象关联；
6. 业务逻辑：也就是调用事件生产者的onData方法发布事件，本文的做法是在单元测试类中发布事件，然后检查消费的事件数和生产的事件数是否一致。

## 三、Hellowrold

本次采用disruptor3.4.4版本，是目前最新版本

### 1. 引入依赖

创建工程(就是一个springboot项目，main方法等省略)，引入依赖：

```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>com.lmax</groupId>
            <artifactId>disruptor</artifactId>
            <version>3.4.4</version>
        </dependency>

        <!-- 糊涂工具包 可以不用，我个人喜好 -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.6.4</version>
        </dependency>
        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.1</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

### 2. 事件的定义

事件定义类StringEvent.java，可见就是个普普通通的java bean

```java
/**
 * 描述: String事件
 *
 * @date: 2021-09-21 22:29
 **/
@Data
public class StringEvent {
    private String value;
}
```

### 3. 事件工厂

就是创建StringEvent实例，并没有特别的操作

```java
import com.collect.disruptor.event.StringEvent;
import com.lmax.disruptor.EventFactory;

/**
 * 描述: 字符串事件工厂
 * <p> 事件工厂的作用，是让disruptor知道如何在内存中创建一个事件实例
 *
 * @date: 2021-09-21 22:30
 **/
public class StringEventFactory implements EventFactory<StringEvent> {

    @Override
    public StringEvent newInstance() {
        return new StringEvent();
    }
}
```

### 4. 事件处理

- 时间处理类的作用是定义一个事件如何被消费，里面是具体的业务代码，每个事件都会执行此类的onEvent方法；
- 本篇的事件处理类做的事情是打印事件内容，再用sleep消耗100毫秒，然后再调用外部传入的Consumer实现类的accept方法。

```java
/**
 * 描述: 事件处理
 * <p> 事件处理类的作用是定义一个事件如何被消费，里面是具体的业务代码，每个事件都会执行此类的onEvent方法；
 *
 * @date: 2021-09-21 22:33
 **/
@Slf4j
public class StringEventHandler implements EventHandler<StringEvent> {

    public StringEventHandler(Consumer<?> consumer) {
        this.consumer = consumer;
    }

    /**
     * 外部可以传入Consumer实现类，每处理一条消息的时候，consumer的accept方法就会被执行一次
     */
    private final Consumer<?> consumer;

    @Override
    public void onEvent(StringEvent event, long sequence, boolean endOfBatch) throws Exception {
        log.info("sequence [{}], endOfBatch [{}], event : {}", sequence, endOfBatch, event);

        // 这里延时100ms，模拟消费事件的逻辑的耗时
        Thread.sleep(100);

        // 如果外部传入了consumer，就要执行一次accept方法
        if (null != consumer) {
            consumer.accept(null);
        }
    }

}
```

### 5. 事件生产者

每当业务要生产一个事件时，就会调用事件生产者的onData方法，将业务数据作为入参传进来，此时生产者会从环形队列中取出一个事件实例（就是前面的事件工厂创建的），把业务数据传给这个实例，再把实例正式发布出去

```java
import com.collect.disruptor.event.StringEvent;
import com.lmax.disruptor.RingBuffer;

/**
 * 描述: 事件生产者
 * <p> 每当业务要生产一个事件时，就会调用事件生产者的onData方法，将业务数据作为入参传进来
 * <p> 此时生产者会从环形队列中取出一个事件实例（就是前面的事件工厂创建的），把业务数据传给这个实例，再把实例正式发布出去
 *
 * @date: 2021-09-21 22:36
 **/
public class StringEventProducer {

    /**
     * 存储数据的环形队列
     */
    private final RingBuffer<StringEvent> ringBuffer;

    public StringEventProducer(RingBuffer<StringEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(String content) {
        // ringBuffer是个队列，其next方法返回的是下最后一条记录之后的位置，这是个可用位置
        long sequence = ringBuffer.next();

        try {
            // sequence位置取出的事件是空事件
            StringEvent stringEvent = ringBuffer.get(sequence);
            // 空事件添加业务信息
            stringEvent.setValue(content);
        } finally {
            // 发布
            ringBuffer.publish(sequence);
        }
    }

}
```

### 6. 初始化逻辑

开发一个spring bean，这里面有disruptor的初始化逻辑，有几处需要关注的地方稍后会说到

```java
import com.collect.disruptor.event.StringEvent;
import com.collect.disruptor.factory.StringEventFactory;
import com.collect.disruptor.handler.StringEventHandler;
import com.collect.disruptor.produce.StringEventProducer;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * 描述: 入门-事件业务
 *
 * @date: 2021-09-21 22:41
 **/
@Service
@Slf4j
public class BasicEventService {

    private static final int BUFFER_SIZE = 16;

    private Disruptor<StringEvent> disruptor;

    private StringEventProducer producer;

    /**
     * 统计消息总数
     */
    private final AtomicLong eventCount = new AtomicLong();

    @PostConstruct
    private void init() {
        Executor executor = Executors.newCachedThreadPool();

        // 实例化
        disruptor = new Disruptor<>(new StringEventFactory(),
                BUFFER_SIZE,
                new CustomizableThreadFactory("event-handler-"));

        // 准备一个匿名类，传给disruptor的事件处理类，
        // 这样每次处理事件时，都会将已经处理事件的总数打印出来
        Consumer<?> eventCountPrinter = (Consumer<Object>) o -> {
            long count = eventCount.incrementAndGet();
            log.info("receive [{}] event", count);
        };

        // 指定处理类
        disruptor.handleEventsWith(new StringEventHandler(eventCountPrinter));

        // 启动
        disruptor.start();

        // 生产者
        producer = new StringEventProducer(disruptor.getRingBuffer());
    }

    public void publish(String value) {
        producer.onData(value);
    }

    public long eventCount() {
        return eventCount.get();
    }

}
```

上述代码有以下几点需要注意：

1. publish方法给外部调用，用于发布一个事件；
2. eventCountPrinter是Consumer的实现类，被传给了StringEventHandler，这样StringEventHandler消费消息的时候，eventCount就会增加，也就记下了已经处理的事件总数；

3. Disruptor的构造方法中，BUFFER_SIZE表示环形队列的大小，这里故意设置为16，这样可以轻易的将环形队列填满，此时再发布事件会不会导致环形队列上的数据被覆盖呢？稍后咱们可以测一下；
4. 记得调用start方法。

### 7. 业务逻辑

现在生产事件的接口已准备好，消费事件的代码也完成了，接下来就是如何调用生产事件的接口来验证生产和消费是否正常，这里我选择使用单元测试来验证；
在test目录下新增测试类BasicEventServiceTest.java，测试逻辑是发布了100个事件，再验证消费事件的数量是否也等于100。

```java
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 描述: 测试
 *
 * @date: 2021-09-21 22:50
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BasicEventServiceTest {

    @Autowired
    BasicEventService basicEventService;

    @Test
    public void publish() throws InterruptedException {
        log.info("start publish test");

        int count = 100;

        for(int i=0;i<count;i++) {
            log.info("publish {}", i);
            basicEventService.publish(String.valueOf(i));
        }

        // 异步消费，因此需要延时等待
        Thread.sleep(1000);
        // 消费的事件总数应该等于发布的事件数
        Assert.assertEquals(count, basicEventService.eventCount());
    }

}
```

### 8. 运行结果

环形数组大小只有16，消费一个事件耗时很长(100毫秒)，那么环形数组中的事件还未消费完时如果还在发布事件会发生什么呢？新事件会覆盖未消费的事件吗？显然不会，因为测试结果是通过的，那么disruptor是怎么做到的呢？其实从日志上可以看出一些端倪，下面是测试过程中日志的末尾部分，一直到测试快结束，发布事件的线程还在执行发布操作，这就意味着：如果消费速度过慢导致环形队列里放不进新的事件时，发布事件的线程就会阻塞，知道环形队列中可以放入事件为止：

```
2021-09-22 08:00:15.774  INFO 5399 --- [event-handler-1] c.c.d.handler.StringEventHandler         : sequence [96], endOfBatch [true], event : StringEvent(value=96)
2021-09-22 08:00:15.774  INFO 5399 --- [           main] c.c.d.service.BasicEventServiceTest      : publish 98
2021-09-22 08:00:15.774  INFO 5399 --- [           main] c.c.d.service.BasicEventServiceTest      : publish 99
2021-09-22 08:00:15.878  INFO 5399 --- [event-handler-1] c.c.disruptor.service.BasicEventService  : receive [97] event
2021-09-22 08:00:15.879  INFO 5399 --- [event-handler-1] c.c.d.handler.StringEventHandler         : sequence [97], endOfBatch [false], event : StringEvent(value=97)
2021-09-22 08:00:15.979  INFO 5399 --- [event-handler-1] c.c.disruptor.service.BasicEventService  : receive [98] event
2021-09-22 08:00:15.979  INFO 5399 --- [event-handler-1] c.c.d.handler.StringEventHandler         : sequence [98], endOfBatch [false], event : StringEvent(value=98)
2021-09-22 08:00:16.080  INFO 5399 --- [event-handler-1] c.c.disruptor.service.BasicEventService  : receive [99] event
2021-09-22 08:00:16.081  INFO 5399 --- [event-handler-1] c.c.d.handler.StringEventHandler         : sequence [99], endOfBatch [true], event : StringEvent(value=99)
2021-09-22 08:00:16.184  INFO 5399 --- [event-handler-1] c.c.disruptor.service.BasicEventService  : receive [100] event
```

