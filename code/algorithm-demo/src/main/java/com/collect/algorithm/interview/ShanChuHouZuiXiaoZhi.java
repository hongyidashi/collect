package com.collect.algorithm.interview;

/**
 * 描述: 删除K后最小值
 *
 * @author: panhongtong
 * @date: 2021-07-21 07:22
 **/
public class ShanChuHouZuiXiaoZhi {

    public static void main(String[] args) {
        System.out.println(removeKDigits("123457", 2));
        System.out.println(removeKDigits("923114", 1));
        System.out.println(removeKDigits("931774821", 3));
    }

    public static String removeKDigits(String num, int k) {
        if (k >= num.length()) {
            return "0";
        }

        int newLen = num.length() - k;
        int[] stack = new int[num.length()];
        int top = 0;

        for (int i = 0; i < num.length(); i++) {
            char c = num.charAt(i);
            // 找到比栈顶大的数字，指针下移，减少k
            if (top > 0 && stack[top - 1] > c && k > 0) {
                top--;
                k--;
            }
            // 压入栈顶
            stack[top++] = c;
        }

        // 找到第一个非零的数字的位置，并以此构建新字符串
        int offset = 0;
        while (offset < newLen && stack[offset] == '0') {
            offset++;
        }
        return offset == newLen ? "0" : new String(stack, offset, newLen - offset);
    }

    /**
     * 效率不高
     *
     * @param num
     * @param k
     * @return
     */
    public static String removeKDigits2(String num, int k) {
        if (k >= num.length()) {
            return "0";
        }

        String numNew = num;

        // for 删除 k 次
        for (int i = 0; i < k; i++) {
            boolean hasCut = false;
            // 从左到右遍历，找到并删除右侧比自己大的数字
            for (int j = 0; j < numNew.length() - 1; j++) {
                if (numNew.charAt(j) > numNew.charAt(j + 1)) {
                    numNew = numNew.substring(0, j) + numNew.substring(j + 1);
                    hasCut = true;
                    break;
                }
            }

            // 如果没有找到，则删除最后一个
            if (!hasCut) {
                numNew = numNew.substring(0, numNew.length() - 1);
            }

            // 清除左侧的 0
            numNew = removeZero(numNew);
        }

        if (numNew.length() == 0) {
            return "0";
        }
        return numNew;
    }

    /**
     * 清除左侧的零
     *
     * @param num 数字字符串
     * @return 清除结果
     */
    public static String removeZero(String num) {
        int count = 0;
        for (int i = 0; i < num.length() - 1; i++) {
            if (num.charAt(i) == '0') {
                count++;
            } else {
                break;
            }
        }
        return num.substring(count);
    }
}
