package com.collect.javase.abstractDemo;

/**
 * @author： peng
 * @date：2021/1/26 14:49
 * @description: 抽象动物
 */
public abstract class Animal {
    public abstract void face();
    abstract void mouse();
    public void print() {
        face();
        mouse();
    }
}
