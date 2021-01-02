package com.collect.multithreading.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 描述: CompeletableFuture Demo6
 * 作者: panhongtong
 * 创建时间: 2021-01-01 22:22
 **/
public class Demo6 {
    public static void main(String[] args) {

        //全部任务都需要执行完
        CompletableFuture<Void> future = CompletableFuture
                .allOf(CompletableFuture.completedFuture("A"),
                        CompletableFuture.completedFuture("B"));
        System.out.println(future.join());

        CompletableFuture<Object> future2 = CompletableFuture
                .anyOf(CompletableFuture.completedFuture("C"),
                        CompletableFuture.completedFuture("D"));
        //其中一个任务行完即可
        System.out.println(future2.join());

    }
}
