package com.collect.algorithm.interview;

/**
 * @description: 最大公约数
 * @author: panhongtong
 * @date: 2022/7/8 10:06
 **/
public class GongYueShu {

    public static void main(String[] args) {
        int m = 25;
        int n = 18;

        // 辗转相除
        int r = m % n;
        while (r != 0) {
            m = n;
            n = r;
            r = m % n;
        }

        System.out.println(n);
    }

}
