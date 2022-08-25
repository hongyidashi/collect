package com.collect.javase.scanner;

import java.util.Scanner;

/**
 * @description: scanner演示
 * @author: panhongtong
 * @date: 2022/8/16 10:24
 **/
public class ScannerDemo2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入一个字符串");
        String str = scanner.next();
        while (!"exit".equals(str)) {
            System.out.println("请继续输入字符串");
            str = scanner.next();
        }
        System.out.println("结束");
    }

}
