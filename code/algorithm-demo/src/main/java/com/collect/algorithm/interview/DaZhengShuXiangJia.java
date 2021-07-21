package com.collect.algorithm.interview;

/**
 * 描述: 大整数相加
 *
 * @author: panhongtong
 * @date: 2021-07-22 07:32
 **/
public class DaZhengShuXiangJia {

    public static void main(String[] args) {
        System.out.println(bigNumSum("1234568", "654322"));
    }

    public static String bigNumSum(String bigNumA, String bigNumB) {
        int lenA = bigNumA.length();
        int lenB = bigNumB.length();

        // 获取最大长度
        int maxLen = lenA > lenB ? lenA + 1 : lenB + 1;

        // 结果数组
        int[] res = new int[maxLen];

        int indexA = lenA - 1;
        int indexB = lenB - 1;
        int temp;
        for (int i = 0; i < maxLen - 1; i++) {
            temp = res[i];
            if ((indexA - i) >= 0) {
                temp = temp + (bigNumA.charAt(indexA - i) - '0');

            }
            if ((indexB - i) >= 0) {
                temp = temp + (bigNumB.charAt(indexB - i) - '0');
            }
            res[i] = temp % 10;
            res[i + 1] = temp / 10;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = res.length - 1; i >= 0; i--) {
            if ((i == res.length - 1) && res[i] == 0) {
                continue;
            }
            sb.append(res[i]);
        }

        return sb.toString();
    }
}
