package com.collect.disruptor.factory;

import com.collect.disruptor.event.StringEvent;
import com.lmax.disruptor.EventFactory;

/**
 * 描述: 字符串事件工厂
 * <p> 事件工厂的作用，是让disruptor知道如何在内存中创建一个事件实例
 *
 * @author: panhongtong
 * @date: 2021-09-21 22:30
 **/
public class StringEventFactory implements EventFactory<StringEvent> {

    @Override
    public StringEvent newInstance() {
        return new StringEvent();
    }
}
