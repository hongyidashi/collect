package com.collect.disruptor.service;

import com.collect.disruptor.event.StringEvent;
import com.collect.disruptor.factory.StringEventFactory;
import com.collect.disruptor.handler.StringEventHandler;
import com.collect.disruptor.produce.StringEventProducer;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * 描述: 入门-事件业务
 *
 * @author: panhongtong
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
