package com.collect.algorithm.search;

/**
 * 描述: RK查找
 *
 * @author: panhongtong
 * @date: 2021-08-10 07:45
 **/
public class RabinKarpSearch {

    public static void main(String[] args) {
        String str = "aacdesadsdfer";
        String pattern = "adsd";
        System.out.println("第一次出现的位置：" + rabinKarp(str, pattern));
    }

    public static int rabinKarp(String str, String pattern) {
        // 主串长度
        int m = str.length();
        // 模式串长度
        int n = pattern.length();
        // 计算模式串的hash
        int patternCode = hash(pattern);
        // 计算主串中第一个和模式串等长的子串hash
        int strCode = hash(str.substring(0, n));
        // 比较
        for (int i = 0; i < (m - n + 1); i++) {
            if ((strCode == patternCode) && compareString(i, str, pattern)) {
                return i;
            }
            // 比较失败，且不是最后一轮，更新主串hash
            if (i < (m - n)) {
                strCode = nextHash(str, strCode, i, n);
            }
        }
        return -1;
    }

    /**
     * 计算hash，采用最简单的方法：a=1 b=2 ... 然后相加
     * @param str
     * @return
     */
    private static int hash(String str) {
        int hashcode = 0;
        for (int i = 0; i < str.length(); i++) {
            hashcode += (str.charAt(i) - 'a');
        }
        return hashcode;
    }

    /**
     * 比较字符串是否相等
     * @param i
     * @param str
     * @param pattern
     * @return
     */
    private static boolean compareString(int i, String str, String pattern) {
        String subStr = str.substring(i, i + pattern.length());
        return subStr.equals(pattern);
    }

    /**
     * 获取下一个子串hash
     * @param str
     * @param hash
     * @param index
     * @param n
     * @return
     */
    private static int nextHash(String str, int hash, int index, int n) {
        hash -= (str.charAt(index) - 'a');
        hash += (str.charAt(index + n) - 'a');
        return hash;
    }
}
