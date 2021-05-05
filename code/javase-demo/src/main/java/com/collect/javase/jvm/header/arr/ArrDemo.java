package com.collect.javase.jvm.header.arr;

import com.collect.javase.jvm.header.User;
import org.openjdk.jol.info.ClassLayout;

/**
 * 描述: 数据demo
 *
 * @author: panhongtong
 * @date: 2021-05-05 23:09
 **/
public class ArrDemo {
    public static void main(String[] args) throws InterruptedException {
        User[] user=new User[2];
        //查看对象的内存布局
        System.out.println(ClassLayout.parseInstance(user).toPrintable());
    }

}
