package com.collect.javase.jvm.instancedata;

import org.openjdk.jol.info.ClassLayout;

/**
 * 描述: 实例数据demo
 *
 * @author: panhongtong
 * @date: 2021-05-05 23:10
 **/
public class InstanceDataDemo {
    public static void main(String[] args) {
        demo1();
    }

    /**
     * 演示子类继承父类前置填充
     */
    private static void demo4() {
        F f = new F();
        System.out.println(ClassLayout.parseInstance(f).toPrintable());
    }

    /**
     * 演示子类字段提前
     */
    private static void demo3() {
        D d = new D();
        System.out.println(ClassLayout.parseInstance(d).toPrintable());
    }

    /**
     * 演示有父类时内存结构
     */
    private static void demo2() {
        B b = new B();
        System.out.println(ClassLayout.parseInstance(b).toPrintable());
    }

    /**
     * 字段排序演示
     */
    private static void demo1() {
        User user = new User();
        //查看对象的内存布局
        System.out.println(ClassLayout.parseInstance(user).toPrintable());
    }
}
