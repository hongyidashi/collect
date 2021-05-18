package com.collect.javase.jvm.unsafe.objopt;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 描述: 对象操作
 *
 * @author: panhongtong
 * @date: 2021-05-18 07:31
 **/
public class ObjOptDemo {

    public static void main(String[] args) throws Exception {
        objTest(getUnsafe());
    }

    public static void objTest(Unsafe unsafe) throws Exception{
        A a1=new A();
        System.out.println(a1.getB());
        A a2 = A.class.newInstance();
        System.out.println(a2.getB());
        A a3= (A) unsafe.allocateInstance(A.class);
        System.out.println(a3.getB());
    }

    public static Unsafe getUnsafe() throws IllegalAccessException, NoSuchFieldException {
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        //Field unsafeField = Unsafe.class.getDeclaredFields()[0]; //也可以这样，作用相同
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        return unsafe;
    }
}
