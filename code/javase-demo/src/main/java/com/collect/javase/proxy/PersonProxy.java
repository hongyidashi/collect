package com.collect.javase.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @description:
 * @author: panhongtong
 * @date: 2022/5/25 20:18
 **/
public class PersonProxy implements InvocationHandler {

    private static final String CRY = "cry";

    private static final Class<?>[] IFACES = new Class<?>[]{Person.class};

    private Person realPerson;

    private Person proxyPerson;

    public PersonProxy(Class<? extends Person> pClazz) {
        try {
            this.realPerson = pClazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.proxyPerson = (Person) Proxy.newProxyInstance(Person.class.getClassLoader(), IFACES, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (CRY.hashCode() == methodName.hashCode() && CRY.equals(methodName)) {
            System.out.println("代理哭哭哭QAQ");
            return null;
        } else {
            System.out.println("我不哭了，你自己哭");
            return method.invoke(realPerson, args);
        }
    }

    public Person getPerson() {
        return this.proxyPerson;
    }
}
