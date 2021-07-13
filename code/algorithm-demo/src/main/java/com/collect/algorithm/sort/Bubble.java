package com.collect.algorithm.sort;

import java.util.Arrays;

/**
 * 描述: 冒泡排序
 *
 * @author: panhongtong
 * @date: 2021-07-13 07:32
 **/
public class Bubble {

    public static void main(String[] args) {
        int[] arr1 = new int[]{4, 1, 9, 3, 5, 6, 7};
        int[] arr2 = new int[]{4, 1, 9, 3, 5, 6, 7};
        int[] arr3 = new int[]{4, 1, 9, 3, 5, 6, 7};
        bubble(arr1);
        bubble2(arr2);
        bubble3(arr3);
    }

    public static void bubble3(int[] arr) {
        int count = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            boolean flag = true;
            int sortBorder = arr.length - 1;
            for (int j = 0; j < sortBorder; j++) {
                int temp;
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    flag = false;
                    sortBorder = j;
                }

            }
            count++;
            if (flag) {
                break;
            }
        }
        System.out.println(Arrays.toString(arr));
        System.out.println("排序次数："+count);
    }

    public static void bubble2(int[] arr) {
        int count = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            boolean flag = true;
            for (int j = 0; j < arr.length - 1; j++) {
                int temp;
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    flag = false;
                }

            }
            count++;
            if (flag) {
                break;
            }
        }
        System.out.println(Arrays.toString(arr));
        System.out.println("排序次数："+count);
    }

    public static void bubble(int[] arr) {
        int count = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1; j++) {
                int temp;
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
            count++;
        }
        System.out.println(Arrays.toString(arr));
        System.out.println("排序次数："+count);
    }

}
