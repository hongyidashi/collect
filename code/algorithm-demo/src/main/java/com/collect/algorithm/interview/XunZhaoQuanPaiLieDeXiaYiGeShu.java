package com.collect.algorithm.interview;

import java.util.Arrays;

/**
 * 描述: 寻找全排列的下一个数
 *
 * @author: panhongtong
 * @date: 2021-07-20 07:35
 **/
public class XunZhaoQuanPaiLieDeXiaYiGeShu {

    public static void main(String[] args) {
        int[] arr = new int[]{1,2,9,4,5};
        int[] res = findNearestNumber(arr);
        System.out.println(Arrays.toString(res));
    }

    public static int[] findNearestNumber(int[] numbers) {
        // 寻找逆序区的第一位
        int index = findTransferPoint(numbers);
        // 说明此时数已经最大
        if (index == 0) {
            return numbers;
        }

        // 将逆序区前一位和逆序区刚刚大于它的交换
        int[] copyArr = Arrays.copyOf(numbers, numbers.length);
        exchangeHand(copyArr, index);

        // 把原来的逆序区变为顺序
        reverse(numbers,index);
        return copyArr;
    }

    /**
     * 寻找逆序区的前一位
     * @param numbers 数组
     * @return
     */
    public static int findTransferPoint(int[] numbers) {
        for (int i = numbers.length - 1; i > 0; i--) {
            if (numbers[i] > numbers[i - 1]) {
                return i;
            }
        }
        // 找不返回0
        return 0;
    }

    /**
     * 将逆序区前一位和比他刚刚大的交换
     * @param numbers 数组
     * @param index 逆序区位置指针
     */
    public static void exchangeHand(int[] numbers, int index) {
        int head = numbers[index - 1];
        for (int i = numbers.length - 1; i >= index; i--) {
            if (head < numbers[i]) {
                numbers[index - 1] = numbers[i];
                numbers[i] = head;
                return;
            }
        }
    }

    public static void reverse(int[] numbers, int index) {
        for (int i = index, j = numbers.length - 1; i < j; i++, j--) {
            int temp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = temp;
        }
    }

}
