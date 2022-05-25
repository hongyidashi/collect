package com.collect.javase.proxy;

/**
 * @description:
 * @author: panhongtong
 * @date: 2022/5/25 20:31
 **/
public class ProxyTest {

    public static void main(String[] args) {
        PersonProxy proxy = new PersonProxy(Boy.class);
        proxy.getPerson().cry();
        proxy.getPerson().hello();
    }

}
