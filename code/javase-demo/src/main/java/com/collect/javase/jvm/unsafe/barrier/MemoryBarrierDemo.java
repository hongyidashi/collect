package com.collect.javase.jvm.unsafe.barrier;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 描述: 内存屏障演示
 *
 * @author: panhongtong
 * @date: 2021-05-17 22:56
 **/
public class MemoryBarrierDemo {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Unsafe unsafe = getUnsafe();
        ChangeThread changeThread = new ChangeThread();
        new Thread(changeThread).start();
        while (true) {
            boolean flag = changeThread.isFlag();
            unsafe.loadFence(); //加入读内存屏障
            if (flag) {
                System.out.println("detected flag changed");
                break;
            }
        }
        System.out.println("main thread end");
    }

    public static Unsafe getUnsafe() throws IllegalAccessException, NoSuchFieldException {
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        //Field unsafeField = Unsafe.class.getDeclaredFields()[0]; //也可以这样，作用相同
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        return unsafe;
    }
}

class ChangeThread implements Runnable {
    /**
     * volatile
     **/
    boolean flag = false;

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("subThread change flag to:" + flag);
        flag = true;
    }

    public boolean isFlag() {
        return flag;
    }
}
