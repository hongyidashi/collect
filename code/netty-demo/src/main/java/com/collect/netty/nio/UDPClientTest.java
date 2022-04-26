package com.collect.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;

/**
 * @description: UDP测试
 * @author: panhongtong
 * @date: 2022/4/25 15:04
 **/
public class UDPClientTest {

    public static void main(String[] args) {
        // 开启UDP channel
        try (DatagramChannel channel = DatagramChannel.open()) {
            // 初始化buffer，内容是hello
            ByteBuffer buffer = StandardCharsets.UTF_8.encode("hello");
            // 绑定服务端
            InetSocketAddress address = new InetSocketAddress("localhost", 9999);
            // 推送消息
            channel.send(buffer, address);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
