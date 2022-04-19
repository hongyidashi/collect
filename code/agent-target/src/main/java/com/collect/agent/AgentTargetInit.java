package com.collect.agent;

/**
 * @description: 模拟的应用初始化的类
 * @author: panhongtong
 * @date: 2022/4/19 10:41
 **/
public class AgentTargetInit {

    public static void init() {
        try {
            System.out.println("APP初始化中...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
