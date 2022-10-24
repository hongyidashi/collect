package com.collect.netty.timer;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: panhongtong
 * @date: 2022/10/14 14:29
 **/
public class WheelTimerTest {

    public static void main(String[] args) throws InterruptedException {
        helloWorld();
//        test();
    }

    public static void test() {
        // 构造一个 Timer 实例
        Timer timer = new HashedWheelTimer();

        task1(timer);
        task2(timer);
    }

    public static void task1(Timer timer) {
        System.out.println("任务执行1:" + LocalDateTime.now());
        timer.newTimeout(timeout -> {
            task1(timer);
        }, 5, TimeUnit.SECONDS);
    }

    public static void task2(Timer timer) {
        System.out.println("任务执行2:" + LocalDateTime.now());
        timer.newTimeout(timeout -> {
            task2(timer);
        }, 3, TimeUnit.SECONDS);
    }

    public static void helloWorld() {
        // 构造一个 Timer 实例
        Timer timer = new HashedWheelTimer();

        // 提交一个任务，让它在 5s 后执行
        Timeout timeout1 = timer.newTimeout(timeout -> System.out.println("5s 后执行该任务"), 5, TimeUnit.SECONDS);

        // 再提交一个任务，让它在 10s 后执行
        Timeout timeout2 = timer.newTimeout(timeout -> System.out.println("10s 后执行该任务"), 10, TimeUnit.SECONDS);

        // 取消掉那个 5s 后执行的任务
        if (!timeout1.isExpired()) {
            timeout1.cancel();
        }

        // 原来那个 5s 后执行的任务，已经取消了。这里我们反悔了，我们要让这个任务在 3s 后执行
        // 我们说过 timeout 持有上、下层的实例，所以下面的 timer 也可以写成 timeout1.timer()
        timer.newTimeout(timeout1.task(), 3, TimeUnit.SECONDS);
    }

}
