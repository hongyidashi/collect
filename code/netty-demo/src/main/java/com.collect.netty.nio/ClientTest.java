package com.collect.netty.nio;

import java.io.IOException;
import java.net.Socket;

/**
 * @description: 测试客户端
 * @author: panhongtong
 * @date: 2022/4/24 14:34
 **/
public class ClientTest {

    public static void main(String[] args) {
        // accept事件
        try (Socket socket = new Socket("localhost", 8080)) {
            System.out.println(socket);
            // read事件
            socket.getOutputStream().write("hello".getBytes());
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

