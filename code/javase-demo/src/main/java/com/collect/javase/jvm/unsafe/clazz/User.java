package com.collect.javase.jvm.unsafe.clazz;

public class User {
    public static String name="Hydra";
    int age;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        User.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}