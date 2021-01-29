package com.collect.javase.reflectionDemo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author： peng
 * @date：2021/1/29 15:34
 * @description: 反射测试demo
 */

class A {
    private int age;
    private String name;
    public A(int age, String name) {
        this.age = age;
        this.name = name;
    }
    private A(String name) {
        this.name = name;
    }
    public String publicToString() {
        System.out.println("public方法：" + name);
        return name;
    }
    private String privateToString() {
        System.out.println("private方法：" + name);
        return name;
    }

    @Override
    public String toString() {
        return "A{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}

public class ReflectionDemo {
    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, NoSuchFieldException {
        demo01();
    }

    /**
     * 测试反射的基本使用
     */
    private static void demo01() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        Class<A> aClass = A.class;
        //获取构造方法，这个方法是private的
        Constructor<A> aConstructor = aClass.getDeclaredConstructor(String.class);
        //允许私有
        aConstructor.setAccessible(true);
        //根据构造方法创建对象
        A a = aConstructor.newInstance("486");
        System.out.println(a);

        //获取方法
        Method privateToString = aClass.getDeclaredMethod("privateToString");
        //允许私有
        privateToString.setAccessible(true);
        //执行方法并能够拿到返回值，要执行执行哪个对象的这个方法
        System.out.println(privateToString.invoke(a));

        //获取私有属性
        Field name = aClass.getDeclaredField("name");
        //允许私有
        name.setAccessible(true);
        //直接变更私有属性，要指定变更哪个对象的属性
        name.set(a, "486");
        System.out.println(a);
    }
}
