package com.collect.netty.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @description: Selector demo
 * @author: panhongtong
 * @date: 2022/4/24 14:40
 **/
public class SelectorTest {

    public static void main(String[] args) throws IOException {
        try (// 创建Selector
             Selector selector = Selector.open();

             // 绑定channel事件（SelectionKey当中有四种事件：OP_ACCEPT，OP_CONNECT, OP_READ, OP_WRITE）
             SocketChannel channel = SocketChannel.open();) {

            channel.configureBlocking(false);
            SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_ACCEPT);

            // 监听channel事件，返回值是发生事件的channel数
            // 监听方法1，阻塞直到绑定事件发生
            int count1 = selector.select();
            // 监听方法2，阻塞直到绑定事件发生，或是超时（时间单位为 ms）
            int count2 = selector.select(1000);
            // 监听方法3，不会阻塞，也就是不管有没有事件，立刻返回，自己根据返回值检查是否有事件
            int count3 = selector.selectNow();
        }

    }
}

