package com.collect.javase.jvm.unsafe.clazz;

import com.collect.javase.jvm.unsafe.UnsafeUtil;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 描述: class操作
 *
 * @author: panhongtong
 * @date: 2021-05-18 07:58
 **/
public class ClazzOptDemo {
    public static void main(String[] args) throws Exception {
        staticTest();
    }

    private static void staticTest() throws Exception {
        Unsafe unsafe = UnsafeUtil.getUnsafe();
        User user=new User();
        System.out.println(unsafe.shouldBeInitialized(User.class));
        Field sexField = User.class.getDeclaredField("name");
        long fieldOffset = unsafe.staticFieldOffset(sexField);
        Object fieldBase = unsafe.staticFieldBase(sexField);
        Object object = unsafe.getObject(fieldBase, fieldOffset);
        System.out.println(object);
    }
}
