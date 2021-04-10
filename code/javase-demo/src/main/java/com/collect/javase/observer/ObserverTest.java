package com.collect.javase.observer;

/**
 * 描述: 测试类
 *
 * @author: panhongtong
 * @date: 2021-04-10 22:52
 **/
public class ObserverTest {
    public static void main(String[] args) {
        // 创建一个观察目标
        TestObservable observable = new TestObservable();

        // 添加观察者
        observable.addObserver(new TestObserver("大福"));
        observable.addObserver(new TestObserver("断腿少女"));
        observable.addObserver(new TestObserver("大傻子"));

        // 操作
        observable.reName("智障");
    }
}
