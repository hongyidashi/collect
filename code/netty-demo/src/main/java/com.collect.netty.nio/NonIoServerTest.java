package com.collect.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: nio服务端
 * @author: panhongtong
 * @date: 2022/4/24 14:18
 **/
public class NonIoServerTest {

    public static void main(String[] args) throws IOException {
        // 使用 nio 来理解非阻塞模式, 单线程
        // 0. ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);
        // 1. 创建了服务器
        try (ServerSocketChannel ssc = ServerSocketChannel.open()) {
            // 非阻塞模式
            ssc.configureBlocking(false);
            // 2. 绑定监听端口
            ssc.bind(new InetSocketAddress(8080));
            // 3. 连接集合
            List<SocketChannel> channels = new ArrayList<>();
            while (true) {
                // 4. accept 建立与客户端连接， SocketChannel 用来与客户端之间通信
                // 非阻塞，线程还会继续运行，如果没有连接建立，但sc是null
                SocketChannel sc = ssc.accept();
                if (sc != null) {
                    System.out.println("connected... " + sc);
                    // 非阻塞模式
                    sc.configureBlocking(false);
                    channels.add(sc);
                }
                for (SocketChannel channel : channels) {
                    // 5. 接收客户端发送的数据
                    // 非阻塞，线程仍然会继续运行，如果没有读到数据，read 返回 0
                    int read = channel.read(buffer);
                    if (read > 0) {
                        buffer.flip();
                        System.out.println(print(buffer));
                        buffer.clear();
                        System.out.println("after read..." + channel);
                    }
                }
                // 用于查非阻塞状态，连接客户端时可关闭，便于观看结果
                System.out.println("wait connecting...");
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
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

