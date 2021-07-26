package com.collect.algorithm.sort;

import java.util.Arrays;

/**
 * 描述: 插入排序
 *
 * @author: panhongtong
 * @date: 2021-07-27 07:40
 **/
public class InsertSort {

    public static void main(String[] args) {
        int[] arr = new int[]{4, 1, 9, 3, 5, 6, 7};
        insertSort(arr);

        System.out.println(Arrays.toString(arr));
    }

    public static void insertSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int insertVal = arr[i];
            int j = i - 1;
            // 找到比插入的要小的，就复制
            while (j >= 0 && insertVal < arr[j]) {
                arr[j + 1] = arr[j];
                j--;
            }
            // 为insertVal找到合适的位置
            arr[j + 1] = insertVal;
        }
    }

}
