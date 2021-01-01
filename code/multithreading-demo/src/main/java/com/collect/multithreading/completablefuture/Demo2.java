package com.collect.multithreading.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 描述: CompeletableFuture Demo2
 * 作者: panhongtong
 * 创建时间: 2021-01-01 22:22
 **/
public class Demo2 {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //method1(executor);
        //method2(executor);
        //method3(executor);
        executor.shutdown();
    }

    /**
     * 任务完成则运行fn，依赖上一个任务的结果，有返回值
     * @param executor
     */
    static void method3(ExecutorService executor) {
        //第一个异步任务，常量任务
        CompletableFuture<String> f = CompletableFuture.completedFuture("OK");
        //第二个异步任务
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> "hello world", executor)
                .thenComposeAsync(data -> {
                    System.out.println(data); return f; //使用第一个任务作为返回
                }, executor);
        System.out.println(future.join());
    }

    /**
     * 任务完成则运行fn，依赖上一个任务的结果，有返回值
     * @param executor
     */
    static void method2(ExecutorService executor) {
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> "hello world", executor)
                .thenApplyAsync(data -> {
                    System.out.println(data); return "OK";
                }, executor);
        System.out.println(future.join());
    }

    /**
     * 任务完成则运行action，不关心上一个任务的结果，无返回值
     * @param executor
     */
    static void method1(ExecutorService executor) {

        CompletableFuture.supplyAsync(() -> "hello 傻逼",executor)
                .thenRunAsync(() -> System.out.println("你个大傻子"),executor);
    }
}
