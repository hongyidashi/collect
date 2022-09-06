package com.collect.javase.designpatterns.chain;

import java.util.Objects;

/**
 * @description: 责任链模式-经理审批(责任链节点2)
 * @author: panhongtong
 * @date: 2022/9/6 22:52
 **/
public class ManagerChain extends CheckChain {

    @Override
    public Boolean proceed(Event event) {
        if (event.getDate() < 7) {
            // 小于7天能处理
            System.out.println("经理审批通过");
            return Boolean.TRUE;
        }
        if (Objects.nonNull(nextChain)) {
            System.out.println("经理无权审批，转到下一节点");
            return nextChain.proceed(event);
        }
        return Boolean.FALSE;
    }

}
