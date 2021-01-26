package com.collect.javase.abstractDemo;

/**
 * @author： peng
 * @date：2021/1/26 14:54
 * @description:
 */
public class Dog extends Animal implements Behavior{

    @Override
    public void face() {
        System.out.println("狗脸");
    }

    @Override
    void mouse() {
        System.out.println("狗嘴");
    }

    @Override
    public void run() {
        System.out.println("狗跑");
    }

    @Override
    public void say() {
        System.out.println("狗叫");
    }
}
