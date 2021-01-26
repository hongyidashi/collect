package com.collect.javase.abstractDemo;

/**
 * @author： peng
 * @date：2021/1/26 15:29
 * @description:
 */
public class InterfaceDemo {
    public static void main(String[] args) {
        Behavior dog = new Dog();
        dog.run();
        dog.say();
        dog.sad();
        Behavior.print();
    }
}
