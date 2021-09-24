package com.collect.javase.func;

/**
 * 描述: 抛异常接口
 *
 * @author: panhongtong
 * @date: 2021-09-24 22:33
 **/
@FunctionalInterface
public interface ThrowExceptionFunction {

    /**
     * 抛出异常信息
     *
     * @param message 异常信息
     **/
    void throwMessage(int code, String message);

}
