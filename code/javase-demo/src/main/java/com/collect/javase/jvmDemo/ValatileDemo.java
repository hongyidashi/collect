package com.collect.javase.jvmDemo;

import java.util.concurrent.TimeUnit;

/**
 * @author： peng
 * @date：2021/1/25 23:07
 * @description:
 */
class A {
    volatile int val = 0;
    public void update() {
        this.val = 486;
    }
}

public class ValatileDemo {

    public static void main(String[] args) {

    }

    /**
     * 测试volatile的可见性
     *      如果不加volatile就是死循环
     *      加了volatile保证了可见性，那么就不会是死循环
     */
    private static void testSeek() {
        A a = new A();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "-come");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            a.update();
            System.out.println(Thread.currentThread().getName() + "-val : " + a.val);
        }, "AAA").start();
        //在AAA线程sleep的时候，main线程获取到的值一直是0
        // 后面就算AAA更新到val到了486，也没人通知main重新从内存中获取新的变量导致死循环
        // 在加volatile之后就能够获取到新的值，即不同线程之间变更的可见性保证
        while(a.val == 0) {

        }
        System.out.println(Thread.currentThread().getName() + "-val : " + a.val);
    }
}
