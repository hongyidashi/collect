package com.collect.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: bio测试
 * @author: panhongtong
 * @date: 2022/4/24 11:08
 **/
public class BioServerTest {

    public static void main(String[] args) throws IOException {
        // 使用 nio 来理解阻塞模式, 单线程
        // 0. ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 1. 创建了服务器
        try (ServerSocketChannel ssc = ServerSocketChannel.open()) {
            // 2. 绑定监听端口
            ssc.bind(new InetSocketAddress(8080));
            // 3. 连接集合
            List<SocketChannel> channels = new ArrayList<>();
            while (true) {
                // 4. accept 建立与客户端连接， SocketChannel 用来与客户端之间通信
                System.out.println("connecting...");
                // 阻塞方法，线程停止运行
                SocketChannel sc = ssc.accept();
                System.out.println("connected... " + sc);
                channels.add(sc);
                for (SocketChannel channel : channels) {
                    // 5. 接收客户端发送的数据
                    System.out.println("before read..." + channel);
                    try {
                        // 阻塞方法，线程停止运行
                        channel.read(buffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    buffer.flip();
                    System.out.println(print(buffer));
                    buffer.clear();
                    System.out.println("after read..." + channel);
                }
            }
        }
    }

    static String print(ByteBuffer b) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < b.limit(); i++) {
            stringBuilder.append((char) b.get(i));
        }
        return stringBuilder.toString();
    }

}
