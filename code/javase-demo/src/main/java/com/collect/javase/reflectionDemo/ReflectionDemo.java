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
    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, NoSuchFieldException, ClassNotFoundException {
        demo02();
    }

    /**
     * 获取Class实例的四种方式
     */
    private static void demo02() throws ClassNotFoundException {
        //1. 通过类获取Class实例
        Class<A> aClass = A.class;
        System.out.println(aClass);

        //2. 通过类实例获取 Class实例
        A a = new A(486, "486");
        Class<? extends A> aClass1 = a.getClass();
        System.out.println(aClass1);

        //3. 通过Class获取某个实例
        Class<?> aClass2 = Class.forName("com.collect.javase.reflectionDemo.A");
        System.out.println(aClass2);

        //4. 通过类加载器获得
        Class<?> aClass3 = ReflectionDemo.class.getClassLoader()
                .loadClass("com.collect.javase.reflectionDemo.A");
        System.out.println(aClass3);

        //本质上他们是相同的，因为运行时类是会被缓存一段时间的
        //而这些都不属于new而是从缓存中获取到这个实例而已
        System.out.println(aClass == aClass1);
        System.out.println(aClass1 == aClass2);
        System.out.println(aClass2 == aClass3);
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
