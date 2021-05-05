package com.collect.javase.enumDemo;

/**
 * @author： peng
 * @date：2021/1/18 12:38
 * @description: java枚举类测试demo 你这些个包 命名劳资不想吐槽了
 */
public class EnumDemo {
    public static void main(String[] args) {
        System.out.println(changliang.ONE);
        System.out.println(daifangfa.ONE.getName());
        System.out.println(EnumInterface.grandFatherEnum.GRAND_FATHER);
        System.out.println(EnumInterface.grandFatherEnum.FatherEnum.FATHER);
        System.out.println(EnumInterface.grandFatherEnum.FatherEnum.sonEnum.SON);
        changliang val = changliang.ONE;

    }

    public void testEnum(changliang val) {
        switch (val) {
            case ONE: System.out.println("1");break;
            case TWO: System.out.println("2");break;
            case THREE: System.out.println("3");break;
        }
    }
}

/**
 * 利用结构来实现类似于分类的目的
 */
interface EnumInterface {
    enum grandFatherEnum implements EnumInterface{
        GRAND_FATHER;
        enum FatherEnum implements EnumInterface{
            FATHER;
            enum sonEnum implements EnumInterface {
                SON
            }
        }
    }
}

/**
 * enum中的方法带常量
 */
enum daifangfa {
    ONE("第一个"), TWO("第二个"), THREE("第三个"), FOUR("第四个");

    private String name;

    private daifangfa(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

/**
 *  enum中定义常量
 */
enum changliang {
    ONE, TWO, THREE, FOUR
}

