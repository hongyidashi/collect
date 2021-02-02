package com.collect.javase.reflectionDemo;

import java.lang.reflect.*;

/**
 * @author： peng
 * @date：2021/1/29 15:34
 * @description: 反射测试demo
 */

class A {
    private int age;
    private String name;
    public A() {

    }
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
    public static void main(String[] args) throws Exception{
        demo04();
    }

    /**
     * 反射对方法的相关操作
     */
    private static void demo04() throws Exception {
        //获取运行时类
        Class<A> aClass = A.class;
        //最常用创建类实例的方式
        A a = aClass.newInstance();
        //获取当前类及其父类的public方法
        Method[] methods = aClass.getMethods();
        //获取当前类的所有方法，包括private，但不包括其父类
        methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            //获取方法的所有注解
            method.getAnnotations();
            method.getDeclaredAnnotations();

            //获取权限
            Modifier.toString(method.getModifiers());

            //获取返回值类型
            method.getReturnType();

            //获取方法名
            method.getName();

            //获取形参类型
            method.getParameterTypes();

            //获取方法throws的异常
            method.getExceptionTypes();

            //方法的执行，视权限不同，可能需要允许访问私有
            if(method.getName().equals("publicToString")) {
                method.setAccessible(true);
                method.invoke(a);
            }
        }
    }

    /**
     * 反射对属性的相关操作
     */
    private static void demo03() throws Exception {
        //获取运行时类
        Class<A> aClass = A.class;
        //最常用创建类实例的方式
        A a = aClass.newInstance();
        //获取所有public属性，包括继承父类的
        Field[] fields = aClass.getFields();
        //获取当前类中的所有属性，包括私有的，但是不包括父类中的属性
        fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            //获取权限，但是权限用012这样代替
            int modifiers = field.getModifiers();
            //将权限转换为一个一个的数据
            Modifier.toString(modifiers);
            //总的来说就是
            String s = Modifier.toString(field.getModifiers());

            //数据类型
            Class<?> type = field.getType();

            //获取属性名
            String name = field.getName();

            //变更属性
            if(field.getName().equals("name")) {
                //允许对私有属性进行操作
                field.setAccessible(true);
                field.set(a, "486");
            }
            System.out.println(s + " " + type + " " + name);
        }
        System.out.println(a);
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
