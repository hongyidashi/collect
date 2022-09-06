package com.collect.javase.designpatterns.chain;

/**
 * @description: 责任链模式-大佬审批(责任链节点3)
 * @author: panhongtong
 * @date: 2022/9/6 22:52
 **/
public class BossChain extends CheckChain {

    @Override
    public Boolean proceed(Event event) {
        System.out.println("大佬审批通过");
        return Boolean.TRUE;
    }

}
