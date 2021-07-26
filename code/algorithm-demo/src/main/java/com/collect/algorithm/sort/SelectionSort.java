package com.collect.algorithm.sort;

import java.util.Arrays;

/**
 * 描述: 选择排序
 * <p> 每次找到最小的值，放到前面
 *
 * @author: panhongtong
 * @date: 2021-07-27 07:31
 **/
public class SelectionSort {

    public static void main(String[] args) {
        int[] arr = new int[]{4, 1, 9, 3, 5, 6, 7};
        selectionSort(arr);

        System.out.println(Arrays.toString(arr));
    }

    public static void selectionSort(int[] arr) {
        // 这里-1是因为最后一个是不需要排序的
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            // 从后一个开始比较，寻找最小值
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            // 如果找到的最小值不是本身，则交换
            if (i != minIndex) {
                int temp = arr[minIndex];
                arr[minIndex] = arr[i];
                arr[i] = temp;
            }
        }
    }
}
