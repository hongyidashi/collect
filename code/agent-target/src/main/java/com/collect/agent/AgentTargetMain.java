package com.collect.agent;

/**
 * @description: 启动类
 * @author: panhongtong
 * @date: 2022/4/19 10:40
 **/
public class AgentTargetMain {

    public static void main(String[] args) {
        System.out.println("目标应用启动...");
        AgentTargetInit.init();
    }

}
