package com.collect.netty.netty.eventloop;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.EventExecutor;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @description: EventLoopGroup
 * @author: panhongtong
 * @date: 2022/4/26 08:40
 **/
public class EventLoopGroupTest {

    public static void main(String[] args) {
//        traverseEventLoopGroup();
        executeTask();
    }

    /**
     * 执行任务
     */
    static void executeTask() {
        // 构造方法可以指定线程数，默认不设置会首先根据Netty的环境变量，否则根据线程核心数*2，最小为1
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(2);

        // 执行普通任务
        nioEventLoopGroup.next().execute(EventLoopGroupTest::print);

        // 执行定时任务,延后一秒打印
        System.out.println(LocalDateTime.now());
        nioEventLoopGroup.next().schedule(EventLoopGroupTest::print, 1000, TimeUnit.MILLISECONDS);
    }

    private static void print() {
        System.out.println(LocalDateTime.now() + " " + Thread.currentThread());
    }

    /**
     * 遍历
     */
    static void traverseEventLoopGroup() {
        // 构造方法可以指定线程数，默认不设置会首先根据Netty的环境变量，否则根据线程核心数*2，最小为1
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(2);

        // 使用期next方法 获取内部的EventLoop
        System.out.println(nioEventLoopGroup.next());
        System.out.println(nioEventLoopGroup.next());
        System.out.println(nioEventLoopGroup.next());

        System.out.println("-------------------------------------");

        // for循环获取内部的EventLoop
        for (EventExecutor group : nioEventLoopGroup) {
            System.out.println(group);
        }
    }
}
