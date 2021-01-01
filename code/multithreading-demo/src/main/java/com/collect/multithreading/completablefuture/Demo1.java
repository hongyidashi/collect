package com.collect.multithreading.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 描述: CompeletableFuture Demo1
 * 作者: panhongtong
 * 创建时间: 2021-01-01 22:11
 **/
public class Demo1 {
    public static void main(String[] args) {
        // 创建一个单线程线程池
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture<Void> rFuture = CompletableFuture
                .runAsync(() -> System.out.println("hello 傻逼"), executor);
        //supplyAsync的使用
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> {
                    System.out.print("hello ");
                    return "傻逼";
                }, executor);

        //阻塞等待，runAsync 的future 无返回值，输出null
        System.out.println(rFuture.join());
        //阻塞等待
        String name = future.join();
        System.out.println(name);
        // 线程池需要关闭
        executor.shutdown();
    }
}
