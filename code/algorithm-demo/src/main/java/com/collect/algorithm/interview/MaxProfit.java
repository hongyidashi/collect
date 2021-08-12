package com.collect.algorithm.interview;

/**
 * 描述: 股票最大收益
 *
 * @author: panhongtong
 * @date: 2021-08-13 07:35
 **/
public class MaxProfit {

    /**
     * 只能卖出一次和买入一次
     *
     * @param prices
     * @return
     */
    public static int maxProfitFor1Time(int[] prices) {
        // 初始化最低价格 和最大收益
        int minPrice = prices[0];
        int maxProfit = 0;

        // 从1开始
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] < minPrice) {
                minPrice = prices[i];
            } else if (prices[i] - minPrice > maxProfit) {
                maxProfit = prices[i] - minPrice;
            }
        }
        return maxProfit;
    }

    /**
     * 不限次数
     *
     * @param prices
     * @return
     */
    public static int maxProfitForAnyTime(int[] prices) {
        int maxProfit = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                maxProfit += prices[i] - prices[i - 1];
            }
        }
        return maxProfit;
    }

    public static void main(String[] args) {
        int[] prices = {9, 2, 7, 4, 3, 1, 8, 4};
        System.out.println("只买卖一次：" + maxProfitFor1Time(prices));
        int[] prices2 = {5, 1, 6, 3, 8, 2, 4, 7};
        System.out.println("不限买卖次数：" + maxProfitForAnyTime(prices2));
    }
}
