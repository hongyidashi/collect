package com.collect.netty.nio;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class StringToBuffer {

    public static void main(String[] args) {
        //字符串转byteBuffer
        ByteBuffer buffer1 = StandardCharsets.UTF_8.encode("你好");
        System.out.println(buffer1);

        //byteBuffer装字符串
        CharBuffer buffer2 = StandardCharsets.UTF_8.decode(buffer1);
        System.out.println(buffer2.getClass());
        System.out.println(buffer2.toString());
    }
}
