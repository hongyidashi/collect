package com.collect.javase.scanner;

import java.util.Scanner;

/**
 * @description:
 * @author: panhongtong
 * @date: 2022/6/19 23:01
 **/
public class ScannerDemo {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入一个数值");
        int i = sc.nextInt();
        System.out.println("输入的数值为:" + i);
        System.out.println("请输入一个字符串");
        String str = sc.next();
        System.out.println("输入的字符串为:" + str);

    }

}
