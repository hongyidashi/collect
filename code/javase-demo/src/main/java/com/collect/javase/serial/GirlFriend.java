package com.collect.javase.serial;

import java.io.Serializable;

/**
 * 描述: 序列化测试类
 * 作者: panhongtong
 * 创建时间: 2021-01-03 22:19
 **/
public class GirlFriend implements Serializable {

    private static String AGE = "18";
    private String name;
    private String size;
    transient private String car;

    private String house;

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "GirlFriend{" +
                "name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", car='" + car + '\'' +
                ", AGE='" + AGE + '\'' +
                '}';
    }
}
