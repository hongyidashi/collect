package com.collect.netty.timer;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;

import java.util.concurrent.TimeUnit;

/**
 * @description: 时间轮算法测试
 * @author: panhongtong
 * @date: 2023/5/16 19:46
 **/
public class WheelTimerTest2 {

    public static void main(String[] args) throws Exception {
        test1();
    }

    static void test1() throws InterruptedException {
        // 构造一个 Timer 实例
        Timer timer = new HashedWheelTimer(1L, TimeUnit.SECONDS, 64);

//        Timeout timeout1 = timer.newTimeout(timeout -> {
//            System.out.println("开始执行任务1");
//            // 休眠5秒
//            Thread.sleep(100000);
//            System.out.println("结束执行任务1");
//        }, 1, TimeUnit.SECONDS);

        Timeout timeout2 = timer.newTimeout(timeout -> {
            System.out.println("开始执行任务2 " + Thread.currentThread().getName());
            System.out.println("结束执行任务2 " + Thread.currentThread().getName());
        }, 1, TimeUnit.SECONDS);

        Timeout timeout3 = timer.newTimeout(timeout -> {
            System.out.println("开始执行任务3 " + Thread.currentThread().getName());
            System.out.println("结束执行任务3 " + Thread.currentThread().getName());
            throw new RuntimeException("主动错误");
        }, 1, TimeUnit.SECONDS);

        Timeout timeout4 = timer.newTimeout(timeout -> {
            System.out.println("开始执行任务4 " + Thread.currentThread().getName());
            System.out.println("结束执行任务4 " + Thread.currentThread().getName());
        }, 2, TimeUnit.SECONDS);

        Timeout timeout5 = timer.newTimeout(timeout -> {
            System.out.println("开始执行任务5 " + Thread.currentThread().getName());
            System.out.println("结束执行任务5 " + Thread.currentThread().getName());
        }, 4, TimeUnit.SECONDS);

        Timeout timeout6 = timer.newTimeout(timeout -> {
            System.out.println("开始执行任务6 " + Thread.currentThread().getName());
            System.out.println("结束执行任务6 " + Thread.currentThread().getName());
        }, 6, TimeUnit.SECONDS);

        Thread.sleep(Integer.MAX_VALUE);
    }
}
