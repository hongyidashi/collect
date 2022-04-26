package com.collect.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * @description: UDP测试
 * @author: panhongtong
 * @date: 2022/4/25 15:04
 **/
public class UDPServerTest {

    public static void main(String[] args) {
        // 开启UDP channel
        try (DatagramChannel channel = DatagramChannel.open()) {
            // 绑定端口
            channel.socket().bind(new InetSocketAddress(9999));
            System.out.println("waiting...");
            ByteBuffer buffer = ByteBuffer.allocate(32);
            // 接收消息，并写入buffer，此处方法阻塞
            channel.receive(buffer);
            // 切换buffer为读模式
            buffer.flip();
            System.out.println(print(buffer));
        } catch (IOException e) {
            e.printStackTrace();
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
