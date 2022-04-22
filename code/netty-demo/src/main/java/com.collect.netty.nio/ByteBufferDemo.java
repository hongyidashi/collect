package com.collect.netty.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @description: ByteBuffer使用演示
 * @author: panhongtong
 * @date: 2022/4/21 08:55
 **/
public class ByteBufferDemo {

    public static void main(String[] args) {
        // 提前准备好文件，并向文件中写入内容
        try (RandomAccessFile file = new RandomAccessFile("/Users/panhongtong/mydir/text.txt", "rw")) {
            FileChannel channel = file.getChannel();
            //申请大小为10的buffer
            ByteBuffer buffer = ByteBuffer.allocate(10);
            do {
                // 向 buffer 写入
                int len = channel.read(buffer);
                System.out.println("\n读到字节数：" + len);
                if (len == -1) {
                    break;
                }
                // 切换 buffer 读模式
                buffer.flip();
                while (buffer.hasRemaining()) {
                    System.out.print((char) buffer.get() + " ");
                }
                // 切换 buffer 写模式
                buffer.clear();
            } while (true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
