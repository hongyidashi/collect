package com.collect.javase.jvm.sync.biaslock;

import com.collect.javase.jvm.sync.User;
import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 描述: 偏向锁演示
 *
 * @author: panhongtong
 * @date: 2021-05-08 07:33
 **/
public class BiasLockDemo {

    private static Thread t1, t2, t3;

    public static void main(String[] args) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);

        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            list.add(new Object());
        }

        t1 = new Thread(() -> {
            for (int i = 0; i < list.size(); i++) {
                synchronized (list.get(i)) {
                }
            }
            LockSupport.unpark(t2);
        });
        t2 = new Thread(() -> {
            LockSupport.park();
            for (int i = 0; i < list.size(); i++) {
                Object o = list.get(i);
                synchronized (o) {
                    if (i == 18 || i == 19) {
                        System.out.println("THREAD-2 Object" + (i + 1) + ":" + ClassLayout.parseInstance(o).toPrintable());
                    }
                }
            }
            LockSupport.unpark(t3);
        });
        t3 = new Thread(() -> {
            LockSupport.park();
            for (int i = 0; i < list.size(); i++) {
                Object o = list.get(i);
                synchronized (o) {
                    System.out.println("THREAD-3 Object" + (i + 1) + ":" + ClassLayout.parseInstance(o).toPrintable());
                }
            }
        });

        t1.start();
        t2.start();
        t3.start();
        t3.join();
        System.out.println("New: " + ClassLayout.parseInstance(new Object()).toPrintable());
    }

    /**
     * 批量重偏向演示
     *
     * @throws InterruptedException
     */
    private static void demo3() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            list.add(new Object());
        }

        t1 = new Thread(() -> {
            for (int i = 0; i < list.size(); i++) {
                synchronized (list.get(i)) {
                }
            }
            LockSupport.unpark(t2);
        });
        t2 = new Thread(() -> {
            LockSupport.park();
            for (int i = 0; i < 30; i++) {
                Object o = list.get(i);
                synchronized (o) {
                    if (i == 18 || i == 19) {
                        System.out.println("THREAD-2 Object" + (i + 1) + ":" + ClassLayout.parseInstance(o).toPrintable());
                    }
                }
            }
        });
        t1.start();
        t2.start();
        t2.join();

        TimeUnit.SECONDS.sleep(3);
        System.out.println("Object19:" + ClassLayout.parseInstance(list.get(18)).toPrintable());
        System.out.println("Object20:" + ClassLayout.parseInstance(list.get(19)).toPrintable());
        System.out.println("Object30:" + ClassLayout.parseInstance(list.get(29)).toPrintable());
        System.out.println("Object31:" + ClassLayout.parseInstance(list.get(30)).toPrintable());
    }

    /**
     * 演示偏向锁升级到重量级锁
     *
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
