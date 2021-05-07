package com.collect.javase.jvm.sync.biaslock;

import com.collect.javase.jvm.sync.User;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/**
 * 描述:
 *
 * @author: panhongtong
 * @date: 2021-05-08 07:33
 **/
public class BiasLockDemo {
    public static void main(String[] args) throws InterruptedException {
        demo2();
    }

    /**
     * 演示偏向锁升级到重量级锁
     * @throws InterruptedException
     */
    private static void demo2() throws InterruptedException {
        User user = new User();
        Thread thread = new Thread(() -> {
            synchronized (user) {
                System.out.println("--THREAD1--:" + ClassLayout.parseInstance(user).toPrintable());
                try {
                    user.wait(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("--THREAD END--:" + ClassLayout.parseInstance(user).toPrintable());
            }
        });
        thread.start();
        thread.join();
        TimeUnit.SECONDS.sleep(3);
        System.out.println(ClassLayout.parseInstance(user).toPrintable());
    }

    /**
     * 演示偏向锁升级为轻量级锁
     *
     * @throws InterruptedException
     */
    private static void demo1() throws InterruptedException {
        // -XX:BiasedLockingStartupDelay=0
        User user = new User();
        synchronized (user) {
            System.out.println(ClassLayout.parseInstance(user).toPrintable());
        }
        Thread thread = new Thread(() -> {
            synchronized (user) {
                System.out.println("--THREAD--:" + ClassLayout.parseInstance(user).toPrintable());
            }
        });
        thread.start();
        thread.join();
        System.out.println("--END--:" + ClassLayout.parseInstance(user).toPrintable());
    }
}
