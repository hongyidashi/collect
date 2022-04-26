package com.collect.netty.nio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: 边界测试-服务端
 * @author: panhongtong
 * @date: 2022/4/25 09:54
 **/
public class BoundaryServerTest {

    public static void main(String[] args) throws IOException {
        try (ServerSocket ss = new ServerSocket(9000)) {
            while (true) {
                Socket s = ss.accept();
                InputStream in = s.getInputStream();
                // 每次读取4个字节
                byte[] arr = new byte[4];
                while (true) {
                    int read = in.read(arr);
                    // 读取长度是-1，则不读取了
                    if (read == -1) {
                        break;
                    }
                    System.out.println(new String(arr, 0, read));
                }
            }
        }
    }
}

