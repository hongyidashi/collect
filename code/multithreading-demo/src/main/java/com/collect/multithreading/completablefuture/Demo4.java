package com.collect.multithreading.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 描述: CompeletableFuture Demo4
 * 作者: panhongtong
 * 创建时间: 2021-01-01 22:22
 **/
public class Demo4 {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        //method1(executor);

        //method2(executor);

        //method3(executor);

        executor.shutdown();
    }

    /**
     * 上一个任务或者other任务完成, 运行fn，依赖最先完成任务的结果，有返回值
     * @param executor
     */
    static void method3(ExecutorService executor) {
        //第一个异步任务，休眠1秒，保证最晚执行晚
        CompletableFuture<String> first = CompletableFuture.supplyAsync(()->{
            try{ Thread.sleep(1000);  }catch (Exception e){}
            return "hello 智障";
        });
        CompletableFuture<String> future = CompletableFuture
                //第二个异步任务
                .supplyAsync(() -> "hello 傻逼", executor)
                // data ->  System.out.println(data) 是第三个任务
                .applyToEitherAsync(first, data ->  {
                    System.out.println(data);
                    return "OK";
                } , executor);
        System.out.println(future.join());
    }

    /**
     * 上一个任务或者other任务完成, 运行action，依赖最先完成任务的结果，无返回值
     * @param executor
     */
    static void method2(ExecutorService executor) {
        //第一个异步任务，休眠1秒，保证最晚执行晚
        CompletableFuture<String> first = CompletableFuture.supplyAsync(()->{
            try{ Thread.sleep(1000);  }catch (Exception e){}
            return "hello 智障";
        });
        CompletableFuture<Void> future = CompletableFuture
                //第二个异步任务
                .supplyAsync(() -> "hello 傻逼", executor)
                // data ->  System.out.println(data) 是第三个任务
                .acceptEitherAsync(first, data ->  System.out.println(data) , executor);
    }

    /**
     * 上一个任务或者other任务完成, 运行action，不依赖前一任务的结果，无返回值
     * @param executor
     */
    static void method1(ExecutorService executor) {
        //第一个异步任务，休眠1秒，保证最晚执行晚
        CompletableFuture<String> first = CompletableFuture.supplyAsync(()->{
            try{ Thread.sleep(1000); } catch (Exception e){}
            System.out.println("hello 智障");
            return "hello world";
        });
        CompletableFuture<Void> future = CompletableFuture
                //第二个异步任务
                .supplyAsync(() ->{
                    System.out.println("hello 傻逼");
                    return "hello 大福";
                } , executor)
                //() ->  System.out.println("OK") 是第三个任务
                .runAfterEitherAsync(first, () ->  System.out.println("OK") , executor);
    }

}
