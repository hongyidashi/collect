package com.collect.javase.designpatterns.chain;

import java.util.Objects;

/**
 * @description: 责任链模式-请假责任链
 * @author: panhongtong
 * @date: 2022/9/6 22:41
 **/
public abstract class CheckChain {

    /**
     * 下一处理人
     */
    protected CheckChain nextChain;

    /**
     * 设置下一责任人
     *
     * @param nextChain 下一责任人
     */
    public void setNextChain(CheckChain nextChain) {
        this.nextChain = nextChain;
    }

    /**
     * 处理事件
     *
     * @param event 事件
     * @return 处理结果
     */
    public abstract Boolean proceed(Event event);

    public static class Builder {
        /**
         * 拦截链头
         */
        private CheckChain head;

        /**
         * 拦截链尾
         */
        private CheckChain tail;

        public Builder addChecker(CheckChain checkChain) {
            if (Objects.isNull(head)) {
                this.head = this.tail = checkChain;
                return this;
            }

            // 设置下一节点
            this.tail.setNextChain(checkChain);
            this.tail = checkChain;
            return this;
        }

        /**
         * 构建
         *
         * @return 返回头节点
         */
        public CheckChain build() {
            return head;
        }
    }
}
