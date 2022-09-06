package com.collect.javase.designpatterns.chain;

/**
 * @description: 责任链-事件
 * @author: panhongtong
 * @date: 2022/9/6 22:44
 **/
public class Event {

    /**
     * 请假天数
     */
    private int date;

    public Event(int date) {
        this.date = date;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}
