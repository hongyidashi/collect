package com.collect.javase.observer;

import java.util.Observable;

/**
 * 描述: 观察者模式demo - 被观察者
 *
 * @author: panhongtong
 * @date: 2021-04-10 22:45
 **/
public class TestObservable extends Observable {

    private String name;

    public void reName(String name) {
        // 命名
        this.name = name;

        // 改变状态
        this.setChanged();

        // 通知所有观察者
        this.notifyObservers();
    }

    public String getName() {
        return name;
    }

}
