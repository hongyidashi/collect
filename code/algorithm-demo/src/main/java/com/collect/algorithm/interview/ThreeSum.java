package com.collect.algorithm.interview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 描述: 三数之和
 *
 * @author: panhongtong
 * @date: 2021-08-12 07:38
 **/
public class ThreeSum {

    public static void main(String[] args) {
        int[] arr = {5, 12, 6, 3, 9, 2, 1, 7};
        List<List<Integer>> result = threeSum(arr, 13);
        for (List<Integer> list : result) {
            System.out.println(list);
        }
    }

    public static List<List<Integer>> threeSum(int[] arr, int target) {
        Arrays.sort(arr);
        List<List<Integer>> result = new ArrayList<>();

        // 大循环
        for (int i = 0; i < arr.length; i++) {
            // 差
            int d = target - arr[i];

            for (int j = i+1,k = arr.length - 1; j < arr.length - 1; j++) {
                // 结果偏大时，k 指针移动
                while (j < k && (arr[k] + arr[j] > d)) {
                    k--;
                }
                // 双指针重合，出本次循环
                if (j == k) {
                    break;
                }
                // 找到结果
                if (arr[j] + arr[k] == d) {
                    result.add(Arrays.asList(arr[j], arr[k], arr[i]));
                }
            }

        }

        return result;
    }

}
