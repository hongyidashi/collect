package com.collect.algorithm.sort;

import java.util.Arrays;

/**
 * 描述: 快速排序
 *
 * @author: panhongtong
 * @date: 2021-07-14 07:40
 **/
public class QuickSort {
    public static void main(String[] args) {
        int[] arr = new int[]{4, 1, 9, 3, 5, 6, 7};
        quickSort(arr, 0, arr.length - 1);

        System.out.println(Arrays.toString(arr));
    }

    /**
     * 快速排序
     * @param arr 数组
     * @param start 起始指针
     * @param end 结束指针
     */
    public static void quickSort(int[] arr, int start, int end) {
        if (start >= end) {
            return;
        }

        int pivotIndex = partition2(arr, start, end);
        quickSort(arr, start, pivotIndex-1);
        quickSort(arr,pivotIndex+1,end);
    }

    public static int partition2(int[] arr, int start, int end) {
        int pivot = arr[start];
        int mark = start;

        for (int i = start + 1; i <= end; i++) {
            if (arr[i] < pivot) {
                mark++;
                int temp = arr[mark];
                arr[mark] = arr[i];
                arr[i] = temp;
            }
        }

        arr[start] = arr[mark];
        arr[mark] = pivot;

        return mark;
    }

    /**
     * 分治
     * @param arr
     * @param start
     * @param end
     * @return 下一次排序的基准值下标
     */
    public static int partition(int[] arr, int start, int end) {
        int pivot = arr[start];
        int left = start;
        int right = end;

        while (left != right) {
            while (left < right && arr[right] > pivot) {
                right--;
            }
            while (left < right && arr[left] <= pivot) {
                left++;
            }
            if (left < right) {
                int temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;
            }
        }

        // 交换指针重合位置和基准值
        arr[start] = arr[left];
        arr[left] = pivot;

        return left;
    }
}
