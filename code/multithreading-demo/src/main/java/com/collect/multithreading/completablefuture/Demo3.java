package com.collect.multithreading.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 描述: CompeletableFuture Demo3
 * 作者: panhongtong
 * 创建时间: 2021-01-01 22:22
 **/
public class Demo3 {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        //method1(executor);

        //method2(executor);

        //method3(executor);

        executor.shutdown();
    }

    /**
     * 两个CompletableFuture[并行]执行完，然后执行action，依赖上两个任务的结果，有返回值
     * @param executor
     */
    static void method3(ExecutorService executor) {
        //第一个异步任务，常量任务
        CompletableFuture<String> first = CompletableFuture.completedFuture("hello world");
        CompletableFuture<String> future = CompletableFuture
                //第二个异步任务
                .supplyAsync(() -> "hello 傻逼", executor)
                // (w, s) -> System.out.println(s) 是第三个任务
                .thenCombineAsync(first, (s, w) -> {
                    System.out.println(s);
                    System.out.println(w);
                    return "OK";
                }, executor);
        System.out.println(future.join());
    }

    /**
     * 两个CompletableFuture[并行]执行完，然后执行action，依赖上两个任务的结果，无返回值
     * @param executor
     */
    static void method2(ExecutorService executor) {
        //第一个异步任务，常量任务
        CompletableFuture<String> first = CompletableFuture.completedFuture("hello world");
        CompletableFuture<Void> future = CompletableFuture
                //第二个异步任务
                .supplyAsync(() -> "hello 傻逼", executor)
                // (w, s) -> System.out.println(s) 是第三个任务
                .thenAcceptBothAsync(first, (s, w) -> {
                        System.out.println(s);
                        System.out.println(w);
                    }, executor);
    }

    /**
     * 两个CompletableFuture[并行]执行完，然后执行action，不依赖上两个任务的结果，无返回值
     * @param executor
     */
    static void method1(ExecutorService executor) {
        //第一个异步任务，常量任务
        CompletableFuture<String> first = CompletableFuture.completedFuture("hello world");
        CompletableFuture<Void> future = CompletableFuture
                //第二个异步任务
                .supplyAsync(() -> "hello siting", executor)
                // () -> System.out.println("OK") 是第三个任务
                .runAfterBothAsync(first, () -> System.out.println("OK"), executor);
    }
}
