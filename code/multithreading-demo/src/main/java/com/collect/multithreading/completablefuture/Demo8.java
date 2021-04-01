package com.collect.multithreading.completablefuture;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 描述:
 *
 * @author: panhongtong
 * @date: 2021-03-26 22:34
 **/
public class Demo8 {
    public static void main(String[] args) throws Exception {
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        ExecutorService executor = Executors.newCachedThreadPool();
//        method1(executor);
//        executor.shutdown();
        method2();
    }

    static void method2() {
        List<String> list = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
        System.out.printf("任务开始;当前时间:%s\n",formatter.format(LocalDateTime.now()));
        CompletableFuture<Void> task1 = CompletableFuture.runAsync(() -> {
            System.out.printf("任务1开始;当前时间:%s\n",formatter.format(LocalDateTime.now()));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list.add("大福");
            System.out.printf("任务1结束;当前时间:%s\n", formatter.format(LocalDateTime.now()));
        });
        CompletableFuture<Void> task2 = CompletableFuture.runAsync(() -> {
            System.out.printf("任务2开始;当前时间:%s\n",formatter.format(LocalDateTime.now()));

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list.add("断腿少女");
            System.out.printf("任务2结束;当前时间:%s\n",formatter.format(LocalDateTime.now()));
        });
        CompletableFuture<Void> task3 = CompletableFuture.runAsync(() -> {
            System.out.printf("任务3开始;当前时间:%s\n",formatter.format(LocalDateTime.now()));
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            list.add("GDX");
            System.out.printf("任务3结束;当前时间:%s\n",formatter.format(LocalDateTime.now()));
        });

        CompletableFuture<Void> future = CompletableFuture.allOf(task1, task2, task3);
        future.join();
        System.out.printf("任务结束;当前时间:%s\n",formatter.format(LocalDateTime.now()));
        System.out.println(list);
    }

    static void method1(ExecutorService executor) throws Exception {
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello world 1";
        },executor);
        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello world 2";
        },executor);
        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello world 3";
        },executor);

        Map<String, String> data = new HashMap<>(3);
        // get()方法会阻塞
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
        data.put("task01",task1.get());
        System.out.printf("task1执行完毕;当前时间:%s\n",formatter.format(LocalDateTime.now()));
        data.put("task02",task2.get());
        System.out.printf("task2执行完毕;当前时间:%s\n",formatter.format(LocalDateTime.now()));
        data.put("task03",task3.get());
        System.out.printf("task3执行完毕;当前时间:%s\n",formatter.format(LocalDateTime.now()));

        System.out.println(data);
    }
}
