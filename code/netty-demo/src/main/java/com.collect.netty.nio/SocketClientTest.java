package com.collect.netty.nio;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @description: socket测试客户端
 * @author: panhongtong
 * @date: 2022/4/24 11:12
 **/
public class SocketClientTest {

    public static void main(String[] args) throws Exception {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost", 8080));
        System.out.println("waiting...");
        //模拟长连接一直存在
        while (true) {
        }
    }

}
