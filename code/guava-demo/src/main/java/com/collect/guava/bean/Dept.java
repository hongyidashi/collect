package com.collect.guava.bean;

/**
 * @description:
 * @author: panhongtong
 * @date: 2022/5/16 21:55
 **/
public class Dept {

    private String name;

    public Dept(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "name='" + name + '\'' +
                '}';
    }
}
