package com.collect.algorithm.sort;

import java.util.Arrays;

/**
 * 描述: 贵宾排序
 *
 * @author: panhongtong
 * @date: 2021-07-28 07:43
 **/
public class MergeSort {

    public static void main(String[] args) {
        int[] arr = new int[]{4, 1, 9, 3, 5, 6, 7};
        mergeSort(arr, 0, arr.length - 1);

        System.out.println(Arrays.toString(arr));
    }

    public static void mergeSort(int[] arr, int start, int end) {
        if (start < end) {
            int mid = (start + end) / 2;
            mergeSort(arr, start, mid);
            mergeSort(arr, mid + 1, end);
            merge(arr, start, mid, end);
        }
    }

    public static void merge(int[] arr, int start, int mid, int end) {
        // 开辟额外的大集合，设置指针
        int[] tempArr = new int[end - start + 1];
        int p1 = start;
        int p2 = mid + 1;
        int p = 0;

        // 比较两个小集合的元素，并加入到中间集合中
        while ((p1 <= mid) && (p2 <= end)) {
            if (arr[p1] <= arr[p2]) {
                tempArr[p++] = arr[p1++];
            } else {
                tempArr[p++] = arr[p2++];
            }
        }

        // 如果有一边集合空了，则将剩余所有的加入到中间集合
        while (p1 <= mid) {
            tempArr[p++] = arr[p1++];
        }
        while (p2 <= end) {
            tempArr[p++] = arr[p2++];
        }

        // 把中间集合复制回原数组
        for (int i = 0; i < tempArr.length; i++) {
            arr[i + start] = tempArr[i];
        }
    }
}
