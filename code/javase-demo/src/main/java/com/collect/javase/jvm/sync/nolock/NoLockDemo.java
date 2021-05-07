package com.collect.javase.jvm.sync.nolock;

import com.collect.javase.jvm.sync.User;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/**
 * 描述: 无锁状态
 *
 * @author: panhongtong
 * @date: 2021-05-06 22:33
 **/
public class NoLockDemo {

    //-XX:BiasedLockingStartupDelay=0  关闭偏向锁延迟
    //-XX:-UseBiasedLocking 关闭偏向锁
    public static void main(String[] args) throws InterruptedException {
        User user=new User();
        synchronized (user){
            System.out.println(ClassLayout.parseInstance(user).toPrintable());
        }
    }

    /**
     * 无锁不偏向状态
     * @throws InterruptedException
     */
    private static void demo1() throws InterruptedException {
        User user2 = new User();
        TimeUnit.SECONDS.sleep(4);
        System.out.println(ClassLayout.parseInstance(user2).toPrintable());
    }
}
