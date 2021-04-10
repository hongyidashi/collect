package com.collect.javase.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * 描述: 观察者模式demo - 被观察者
 *
 * @author: panhongtong
 * @date: 2021-04-10 22:48
 **/
public class TestObserver implements Observer {

    private String name;

    @Override
    public void update(Observable o, Object arg) {
        acction(o);
    }

    private void acction(Observable o) {
        TestObservable observable = (TestObservable) o;
        System.out.println(name + "按奈不住采取了行动，操作了" + observable.getName());
    }

    public TestObserver(String name) {
        this.name = name;
    }
}
