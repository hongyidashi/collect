package com.collect.algorithm.interview;

import java.util.Arrays;

/**
 * 描述: 打印矩阵
 *
 * @author: panhongtong
 * @date: 2021-08-09 07:26
 **/
public class DaYinJuZhen {

    public static void main(String[] args) {
        print(4);
    }

    private static void print(int n) {
        int[][] res = new int[n][n];
        int index1 = 0;
        int index2 = 0;
        int val = 1;
        res[index1][index2] = val;
        boolean flag = true;

        while (flag) {
            flag = false;
            while (index2 + 1 < n && (res[index1][index2 + 1] == 0)) {
                res[index1][index2+1] = ++val;
                index2++;
                flag = true;
            }

            while (index1 + 1 < n && (res[index1 + 1][index2] == 0)) {
                res[index1 + 1][index2] = ++val;
                index1++;
                flag = true;
            }

            while (index2 - 1 >= 0 && (res[index1][index2 - 1] == 0)) {
                res[index1][index2 - 1] = ++val;
                index2--;
                flag = true;
            }

            while (index1 - 1 >= 0 && (res[index1 - 1][index2] == 0)) {
                res[index1 - 1][index2] = ++val;
                index1--;
                flag = true;
            }
        }

        for (int[] re : res) {
            System.out.println(Arrays.toString(re));
        }
    }

}
