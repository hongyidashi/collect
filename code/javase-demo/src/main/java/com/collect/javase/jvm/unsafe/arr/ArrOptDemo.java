package com.collect.javase.jvm.unsafe.arr;

import com.collect.javase.jvm.unsafe.UnsafeUtil;
import sun.misc.Unsafe;

/**
 * 描述: 数组操作
 *
 * @author: panhongtong
 * @date: 2021-05-18 07:35
 **/
public class ArrOptDemo {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        arrayTest(UnsafeUtil.getUnsafe());
    }

    private static void arrayTest(Unsafe unsafe) {
        String[] array = new String[]{"str1str1str", "str2", "str3"};
        int baseOffset = unsafe.arrayBaseOffset(String[].class);
        System.out.println(baseOffset);
        int scale = unsafe.arrayIndexScale(String[].class);
        System.out.println(scale);

        for (int i = 0; i < array.length; i++) {
            int offset = baseOffset + scale * i;
            System.out.println(offset + " : " + unsafe.getObject(array, offset));
        }
    }
}
