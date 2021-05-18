package com.collect.javase.jvm.unsafe.clazz;

import com.collect.javase.jvm.unsafe.UnsafeUtil;
import sun.misc.Unsafe;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;

/**
 * 描述: class操作
 *
 * @author: panhongtong
 * @date: 2021-05-18 07:58
 **/
public class ClazzOptDemo {
    public static void main(String[] args) throws Exception {
        defineTest();
    }

    private static void defineTest() throws NoSuchFieldException, IllegalAccessException {
        Unsafe unsafe = UnsafeUtil.getUnsafe();
        String fileName="/Users/panhongtong/work/IDEA-workspace/collect/code/javase-demo/target/classes/com/collect/javase/jvm/unsafe/clazz/User.class";
        File file = new File(fileName);
        try(FileInputStream fis = new FileInputStream(file)) {
            byte[] content=new byte[(int)file.length()];
            fis.read(content);
            Class clazz = unsafe.defineClass(null, content, 0, content.length, null, null);
            Object o = clazz.newInstance();
            Object age = clazz.getMethod("getAge").invoke(o, null);
            System.out.println(age);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void staticTest() throws Exception {
        Unsafe unsafe = UnsafeUtil.getUnsafe();
//        User user=new User();
        System.out.println(unsafe.shouldBeInitialized(User.class));
        Field sexField = User.class.getDeclaredField("name");
        long fieldOffset = unsafe.staticFieldOffset(sexField);
        Object fieldBase = unsafe.staticFieldBase(sexField);
        Object object = unsafe.getObject(fieldBase, fieldOffset);
        System.out.println(object);
    }
}
