package com.collect.javase.jvmDemo;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author： peng
 * @date：2021/1/25 23:07
 * @description: 你这些个包 命名劳资不想吐槽了
 */
class A {
    volatile int val = 0;
    //默认是0，并且保证原子性
    AtomicInteger atomicInteger = new AtomicInteger();
    public void update() {
        this.val = 486;
    }
}

class B {
    private volatile static B b = null;
    private B() {
        System.out.println(Thread.currentThread().getName() + "-构造方法被执行");
    }
    public static B getInstance() {
        if(b == null) {
            //dcl 双端检查锁机制
            synchronized (B.class) {
                if(b == null) {
                    //其实分为了三部，分配内存空间，创建对象，指向内存空间
                    //如果重排的话，也不是很安全
                    b = new B();
                }
            }
        }
        return b;
    }
}
public class ValatileDemo {

    public static void main(String[] args) {
        testDanLi();
    }

    /**
     * 测试指令重排案例，以单例模式举例子
     */
    public static void testDanLi() {
        for(int i = 0; i < 10; i++) {
            new Thread(() -> {
                B b = B.getInstance();
            }, ""+i).start();
        }

    }

    /**
     * 测试非原子性
     *      val++ 其实分为了三步，一个是取val的值，然后val+1.然后赋值给val
     *      如果线程数量比较多的时候，两个线程同时取到了val=0
     *      两个线程几乎同时在自己的工作空间修改为1
     *      这个时候再不保证原子性的情况下，就会出现数据的错误写问题相当于少写了一次
     * 如何保证原子性
     *      通过synchronized修饰，杀鸡用牛刀
     *      通过AtomicInteger来去执行原子性操作
     */
    private static void testNoAtom() {
        A a = new A();
        for(int i = 0; i < 20; i++) {
            //注意这里的线程数最好设置大点，然后里面++的次数也尽量放大
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    a.val++;
                    //getAndIncrement也是自增方法，不过保证了原子性
                    a.atomicInteger.getAndIncrement();
                }
            }, i+"").start();
        }

        //默认是有两个线程，一个是main，一个是gc线程，所以这里得是2
        while(Thread.activeCount() > 2) {
            Thread.yield();
        }

        //最终结果小于2000
        System.out.println("普通的int类型：" + a.val);
        System.out.println("保证原子性的AtomicInteger类型：" + a.atomicInteger);
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
