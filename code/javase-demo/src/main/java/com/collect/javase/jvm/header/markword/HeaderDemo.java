package com.collect.javase.jvm.header.markword;

import com.collect.javase.jvm.header.User;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/**
 * 描述: markword demo
 *
 * @author: panhongtong
 * @date: 2021-05-05 09:59
 **/
public class HeaderDemo {
    public static void main(String[] args) throws InterruptedException {
        demo0();
    }

    /**
     * 验证hashcode延迟加载
     */
    private static void demo5() {
        User user = new User();
        //打印内存布局
        System.out.println(ClassLayout.parseInstance(user).toPrintable());
        //计算hashCode
        System.out.println(user.hashCode());
        //再次打印内存布局
        System.out.println(ClassLayout.parseInstance(user).toPrintable());
    }

    /**
     * 演示升级为重量级锁
     */
    private static void demo4() {
        User user = new User();
        new Thread(() -> {
            synchronized (user) {
                System.out.println("--THREAD1--:" + ClassLayout.parseInstance(user).toPrintable());
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            synchronized (user) {
                System.out.println("--THREAD2--:" + ClassLayout.parseInstance(user).toPrintable());
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 演示偏向锁失效被打破
     *
     * @throws InterruptedException
     */
    private static void demo3() throws InterruptedException {
        User user = new User();
        synchronized (user) {
            System.out.println("--MAIN--:" + ClassLayout.parseInstance(user).toPrintable());
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

    /**
     * 演示一个线程获取偏向锁
     */
    private static void demo2() {
        User user = new User();
        synchronized (user) {
            System.out.println(ClassLayout.parseInstance(user).toPrintable());
        }
        System.out.println(ClassLayout.parseInstance(user).toPrintable());
        synchronized (user) {
            System.out.println(ClassLayout.parseInstance(user).toPrintable());
        }
    }

    /**
     * 观察偏向锁标志位
     *
     * @throws InterruptedException
     */
    private static void demo1() throws InterruptedException {
        User user = new User();
        //查看对象的内存布局
        System.out.println(ClassLayout.parseInstance(user).toPrintable());
        Thread.sleep(4000);
        User user2 = new User();
        //查看对象的内存布局
        System.out.println(ClassLayout.parseInstance(user2).toPrintable());
    }

    private static void demo0() {
        User user = new User();
        //查看对象的内存布局
        System.out.println(ClassLayout.parseInstance(user).toPrintable());
    }

}
