package com.collect.javase.func;

import java.util.function.Function;

/**
 * 描述: 分支处理接口-有返回值
 *
 * @author: panhongtong
 * @date: 2021-09-24 22:51
 **/
@FunctionalInterface
public interface BranchValueHandle<T, R> {

    /**
     * 分支操作
     *
     * @param trueHandle  为true时要进行的操作
     * @param falseHandle 为false时要进行的操作
     **/
    R branch(Function<T, R> trueHandle, Function<T, R> falseHandle);

}
