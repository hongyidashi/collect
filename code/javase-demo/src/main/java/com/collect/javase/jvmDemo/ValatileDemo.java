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
        testNoAtom();
    }

    /**
     * 测试非原子性
     *      val++ 其实分为了三步，一个是取val的值，然后val+1.然后赋值给val
     *      如果线程数量比较多的时候，两个线程同时取到了val=0
     *      两个线程几乎同时在自己的工作空间修改为1
     *      这个时候再不保证原子性的情况下，就会出现数据的错误写问题相当于少写了一次
     */
    private static void testNoAtom() {
        A a = new A();
        for(int i = 0; i < 20; i++) {
            //注意这里的线程数最好设置大点，然后里面++的次数也尽量放大
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    a.val++;
                }
            }, i+"").start();
        }

        //默认是有两个线程，一个是main，一个是gc线程，所以这里得是2
        while(Thread.activeCount() > 2) {
            Thread.yield();
        }

        //最终结果小于2000
        System.out.println(a.val);
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
