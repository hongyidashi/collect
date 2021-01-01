package com.collect.multithreading.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 描述: CompeletableFuture Demo5
 * 作者: panhongtong
 * 创建时间: 2021-01-01 22:22
 **/
public class Demo5 {
    public static void main(String[] args) {

        //method1();

        //method2();

        //method3();

    }

    /**
     * whenComplete-任务完成或者异常时运行action，有返回值：异常
     */
    static void method3() {
        CompletableFuture<AtomicBoolean> first = CompletableFuture
                .supplyAsync(() -> {
                    if (true) {  throw new RuntimeException("发生了异常弟弟"); }
                    return "hello world";
                })
                .thenApply(data -> new AtomicBoolean(false))
                .whenCompleteAsync((data,e) -> {
                    //异常捕捉处理, 但是异常还是会在外层复现
                    System.out.println(e.getMessage());
                });
        System.out.println(first.join());
    }

    /**
     * 相比exceptionally而言，即可处理上一环节的异常也可以处理其正常返回值
     * 输出结果：null 2
     */
    static void method2() {
        CompletableFuture<Integer> first = CompletableFuture
                .supplyAsync(() -> {
                    if (true) { throw new RuntimeException("发生了异常弟弟"); }
                    return "hello world";
                })
                .thenApply(data -> 1)
                .handleAsync((data,e) -> {
                    // 异常捕捉处理
                    e.printStackTrace();
                    System.out.println(data);
                    return 2;
                });
        System.out.println(first.join());
    }

    /**
     * exceptionally-处理异常
     * 如果之前的处理环节有异常问题，则会触发exceptionally的调用相当于 try...catch
     */
    static void method1() {
        CompletableFuture<Integer> first = CompletableFuture
                .supplyAsync(() -> {
                    if (true) {
                        throw new RuntimeException("发生了异常弟弟");
                    }
                    return "hello world";
                })
                .thenApply(data -> 1)
                .exceptionally(e -> {
                    e.printStackTrace(); // 异常捕捉处理，前面两个处理环节的异常都能捕获
                    return 0;
                });
    }


}
