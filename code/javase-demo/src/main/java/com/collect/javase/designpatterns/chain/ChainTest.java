package com.collect.javase.designpatterns.chain;

/**
 * @description: 责任链测试
 * @author: panhongtong
 * @date: 2022/9/6 22:57
 **/
public class ChainTest {

    public static void main(String[] args) {
        CheckChain chain = new CheckChain.Builder()
                .addChecker(new GroupChain())
                .addChecker(new ManagerChain())
                .addChecker(new BossChain())
                .build();
        chain.proceed(new Event(7));
    }

}
