package com.collect.netty.nio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @description: 边界测试-客户端
 * @author: panhongtong
 * @date: 2022/4/25 09:55
 **/
public class BoundaryClientTest {
    public static void main(String[] args) throws IOException {
        Socket max = new Socket("localhost", 9000);
        OutputStream out = max.getOutputStream();
        out.write("hello".getBytes());
        out.write("world".getBytes());
        out.write("你好".getBytes());
        out.write("世界".getBytes());
        max.close();
    }
}
