package com.collect.javase.jvm.unsafe.thread;

import com.collect.javase.jvm.unsafe.UnsafeUtil;
import sun.misc.Unsafe;

import java.util.concurrent.TimeUnit;

/**
 * 描述: 线程操作
 *
 * @author: panhongtong
 * @date: 2021-05-18 07:52
 **/
public class ThreadOptDemo {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Unsafe unsafe = UnsafeUtil.getUnsafe();
        Thread mainThread = Thread.currentThread();
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println("subThread try to unpark mainThread");
                unsafe.unpark(mainThread);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        System.out.println("park main mainThread");
        unsafe.park(false,0L);
        System.out.println("unpark mainThread success");
    }
}
