package com.collect.javase.func;

/**
 * 描述: 分支处理接口
 *
 * @author: panhongtong
 * @date: 2021-09-24 22:51
 **/
@FunctionalInterface
public interface BranchHandle {

    /**
     * 分支操作
     *
     * @param trueHandle  为true时要进行的操作
     * @param falseHandle 为false时要进行的操作
     **/
    void branch(Runnable trueHandle, Runnable falseHandle);

}
