package com.collect.javase.jvm.sync.lightweightlock;

import com.collect.javase.jvm.sync.User;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 描述: 轻量级锁演示
 *
 * @author: panhongtong
 * @date: 2021-05-09 09:34
 **/
public class LightweightLockDemo {

    static Object yesLock;

    public static void main(String[] args) throws InterruptedException {
        yesLock = new Object();
        System.out.println("无锁时对象布局：" + ClassLayout.parseInstance(yesLock).toPrintable());
        IntStream.rangeClosed(1, 4).forEach(i -> {
            getYesLock();
        });
        Thread.sleep(5000L);
        System.out.println("无竞争之后，此时的对象布局：" + ClassLayout.parseInstance(yesLock).toPrintable());
        getYesLock();//此时再来一次加锁
    }

    private static void getYesLock() {
        new Thread(() -> {
            try {
                synchronized (yesLock) {
                    System.out.println("线程[" + Thread.currentThread().getName() + "]" +
                            ":重量级锁状态对象布局:" + ClassLayout.parseInstance(yesLock).toPrintable());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 演示轻量级锁升级 失败
     *
     * @throws InterruptedException
     */
    private static void demo1() throws InterruptedException {
        User user = new User();
        System.out.println("--MAIN--:" + ClassLayout.parseInstance(user).toPrintable());
        Thread thread1 = new Thread(() -> {
            synchronized (user) {
                System.out.println("--THREAD1--:" + ClassLayout.parseInstance(user).toPrintable());
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (user) {
                System.out.println("--THREAD2--:" + ClassLayout.parseInstance(user).toPrintable());
            }
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        TimeUnit.SECONDS.sleep(3);
        System.out.println(ClassLayout.parseInstance(user).toPrintable());
    }
}
