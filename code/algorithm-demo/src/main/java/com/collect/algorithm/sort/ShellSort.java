package com.collect.algorithm.sort;

import java.util.Arrays;

/**
 * 描述: 希尔排序
 *
 * @author: panhongtong
 * @date: 2021-07-28 07:31
 **/
public class ShellSort {

    public static void main(String[] args) {
        int[] arr = new int[]{4, 1, 9, 3, 5, 6, 7};
        shellSort(arr);

        System.out.println(Arrays.toString(arr));
    }

    public static void shellSort(int[] arr) {
        // 希尔增量
        int d = arr.length;

        while (d > 1) {
            d = d / 2;
            // 这层for循环，表示要排序x次
            for (int x = 0; x < d; x++) {
                for (int i = x + d; i < arr.length; i = i + d) {
                    int temp = arr[i];
                    int j;
                    for (j = i - d; (j >= 0) && (arr[j] > temp); j = j - d) {
                        arr[j+d] = arr[j];
                    }
                    arr[j + d] = temp;
                }
            }
        }
    }
}
