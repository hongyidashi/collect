package com.collect.disruptor.handler;

import com.collect.disruptor.event.StringEvent;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * 描述: 事件处理
 * <p> 事件处理类的作用是定义一个事件如何被消费，里面是具体的业务代码，每个事件都会执行此类的onEvent方法；
 *
 * @author: panhongtong
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
