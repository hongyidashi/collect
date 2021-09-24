package com.collect.javase.func;

import java.util.function.Consumer;

/**
 * 描述: 非空消费处理器
 *
 * @author: panhongtong
 * @date: 2021-09-25 07:34
 **/
@FunctionalInterface
public interface PresentOrElseHandler<T extends Object> {

    /**
     * 值不为空时执行消费操作
     * 值为空时执行其他的操作
     *
     * @param action      值不为空时，执行的消费操作
     * @param emptyAction 值为空时，执行的操作
     **/
    void presentOrElseHandle(Consumer<? super T> action, Runnable emptyAction);

}
