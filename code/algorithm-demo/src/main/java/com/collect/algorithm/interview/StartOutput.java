package com.collect.algorithm.interview;

import java.util.Arrays;

/**
 * @description: 星星输出
 * @author: panhongtong
 * @date: 2022/7/8 10:20
 **/
public class StartOutput {

    public static void main(String[] args) {
        String output = "hello baby start!!";

        char[] chars = output.toCharArray();
        char[] startChars = new char[chars.length];

        // 初始化
        Arrays.fill(startChars, '*');

        int charsMaxIndex = chars.length - 1;
        for (int i = 0; (i <= chars.length / 2) && (charsMaxIndex >= i); i++, charsMaxIndex--) {
            if (charsMaxIndex < i) {
                return;
            }
            startChars[i] = chars[i];
            startChars[charsMaxIndex] = chars[charsMaxIndex];
            System.out.println(String.valueOf(startChars));
        }
    }

}
