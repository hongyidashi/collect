package com.collect.javase.jvm.unsafe.basis;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 描述:
 *
 * @author: panhongtong
 * @date: 2021-05-17 07:52
 **/
public class BasisDemo {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        memoryTest(getUnsafe());
    }

    private static void memoryTest(Unsafe unsafe) {
        int size = 4;
        long addr = unsafe.allocateMemory(size);
        long addr3 = unsafe.reallocateMemory(addr, size * 2);
        System.out.println("addr: " + addr);
        System.out.println("addr3: " + addr3);
        try {
            unsafe.setMemory(null, addr, size, (byte) 1);
            for (int i = 0; i < 2; i++) {
                unsafe.copyMemory(null, addr, null, addr3 + size * i, 4);
            }
            System.out.println(unsafe.getInt(addr));
            System.out.println(unsafe.getLong(addr3));
        } finally {
            unsafe.freeMemory(addr);
            unsafe.freeMemory(addr3);
        }
    }

    public static void fieldTest(Unsafe unsafe) throws NoSuchFieldException {
        // 注意，这里的user age字段必须是int，不能是Integer，否则会空指针
        User user = new User();
        long fieldOffset = unsafe.objectFieldOffset(User.class.getDeclaredField("age"));
        System.out.println("offset:" + fieldOffset);
        unsafe.putInt(user, fieldOffset, 20);
        System.out.println("age:" + unsafe.getInt(user, fieldOffset));
        System.out.println("age:" + user.getAge());
    }

    public static Unsafe getUnsafe() throws IllegalAccessException, NoSuchFieldException {
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        //Field unsafeField = Unsafe.class.getDeclaredFields()[0]; //也可以这样，作用相同
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        return unsafe;
    }
}
