package com.collect.javase.func;

/**
 * 描述:
 *
 * @author: panhongtong
 * @date: 2021-09-24 22:23
 **/
public class TestFunc {

    public static void main(String[] args) {
        testPresent();
    }

    public static void testPresent() {
        FuncUtils.isNotNull("大福").presentOrElseHandle(
                obj -> System.out.println(obj + "倒了(哭腔)"),
                () -> System.out.println("没有大福")
        );
    }

    public static void testBranchValue() {
        String result = (String) FuncUtils.condition(true, "断腿少女").branch(
                name -> name + "你个大傻子",
                name -> name + "你个智障");
        System.out.println(result);
    }

    public static void testBranch() {
        FuncUtils.condition(true).branch(
                () -> System.out.println("诶嘿，如果是true就会执行到我"),
                () -> System.out.println("嘿哈，如果是true就会执行到我")
        );
    }

    public static void testEx() {
        FuncUtils.isThrow(false).throwMessage(-1000, "我不会出现，别等了");
        FuncUtils.isThrow(true).throwMessage(-2000, "我，如期而至");
    }

}
