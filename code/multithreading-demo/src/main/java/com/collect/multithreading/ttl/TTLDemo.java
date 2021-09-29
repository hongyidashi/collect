package com.collect.multithreading.ttl;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 描述:
 *
 * @author: panhongtong
 * @date: 2021-09-29 22:18
 **/
public class TTLDemo {

    private final static TransmittableThreadLocal<String> TTL = new TransmittableThreadLocal<>();
    private final static ThreadLocal<String> TL = new ThreadLocal<>();
    private final static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        TTL.set("主线程 ThreadLocal");
        TL.set("主线程 ThreadLocal");
        Runnable task = () -> {
            System.out.println("TTL:" + TTL.get());
            System.out.println("TL:" + TL.get());
        };
        EXECUTOR_SERVICE.execute(task);
        EXECUTOR_SERVICE.execute(TtlRunnable.get(task));
    }

}
