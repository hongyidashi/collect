package com.collect.javase.reflectionDemo;

import java.lang.reflect.Proxy;

/**
 * @author： peng
 * @date：2021/2/3 15:02
 * @description: 反射的应用，动态代理 你这些个包 命名劳资不想吐槽了
 */

interface Animal {
    void eat(String foot);
    void say();
}

class Dog implements Animal {

    @Override
    public void eat(String foot) {
        System.out.println("狗在吃" + foot);
    }

    @Override
    public void say() {
        System.out.println("狗叫");
    }
}

class ProxyFactory {
    public static <E>  E getProxyInstance(E obj) {
        System.out.println("代理前缀处理中");
        //实现动态代理的两个关键点，获取动态代理对象newProxyInstance，
        // 通过调用动态代理对象转到真正的被代码对象的方法上 InvocationHandler
        //获取动态代理对象，一种处理的handler为InvocationHandler的实现类
        return (E) Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    return method.invoke(obj, args);
                }
        );
    }
}

public class ProxyDemo {
    public static void main(String[] args) {
        Animal dog = new Dog();
        //注意，这里在转换类型的时候，代理类不能够跟被代理类是同个对象，不然就没有代理的意思了
        //reflectionDemo.$Proxy0 cannot be cast to Dog
        Animal proxyInstance = ProxyFactory.getProxyInstance(dog);
        proxyInstance.say();
        proxyInstance.eat("骨头");
    }
}
