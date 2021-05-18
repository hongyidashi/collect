package com.collect.javase.jvm.unsafe.cas;

import com.collect.javase.jvm.unsafe.UnsafeUtil;
import sun.misc.Unsafe;

/**
 * 描述: CAS操作
 *
 * @author: panhongtong
 * @date: 2021-05-18 07:43
 **/
public class CASDemo {

    static Unsafe unsafe;

    static {
        try {
            unsafe = UnsafeUtil.getUnsafe();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private volatile int a;

    public static void main(String[] args) {
        CASDemo casTest = new CASDemo();
        new Thread(() -> {
            for (int i = 1; i < 5; i++) {
                casTest.increment(i);
                System.out.print(casTest.a + " ");
            }
        }).start();
        new Thread(() -> {
            for (int i = 5; i < 10; i++) {
                casTest.increment(i);
                System.out.print(casTest.a + " ");
            }
            System.out.println();
        }).start();
    }

    private void increment(int x) {
        while (true) {
            try {
                long fieldOffset = unsafe.objectFieldOffset(CASDemo.class.getDeclaredField("a"));
                if (unsafe.compareAndSwapInt(this, fieldOffset, x - 1, x)) {
                    break;
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }
}
