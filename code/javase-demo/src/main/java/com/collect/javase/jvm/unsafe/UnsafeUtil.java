package com.collect.javase.jvm.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 描述:
 *
 * @author: panhongtong
 * @date: 2021-05-18 07:36
 **/
public class UnsafeUtil {

    public static Unsafe getUnsafe() throws IllegalAccessException, NoSuchFieldException {
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        //Field unsafeField = Unsafe.class.getDeclaredFields()[0]; //也可以这样，作用相同
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        return unsafe;
    }

}
