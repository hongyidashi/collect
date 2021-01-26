package com.collect.javase.abstractDemo;

/**
 * @author： peng
 * @date：2021/1/26 15:27
 * @description: 行为接口
 */
public interface Behavior {
    int num = 0;
    public void run();
    void say();
    static void print(){
        System.out.println("print");
    }
    default void sad() {
        System.out.println("sad");
    }
}
