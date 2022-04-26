package com.collect.netty.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @description: 分散读写
 * @author: panhongtong
 * @date: 2022/4/22 10:36
 **/
public class ScatteringReadWrite {

    public static void main(String[] args) {
        scatteringRead();
        System.out.println("---------------------");
        scatteringWrite();
    }

    /**
     * 分散读
     */
    static void scatteringRead() {
        try (RandomAccessFile file = new RandomAccessFile("/Users/panhongtong/mydir/text.txt", "rw")) {
            FileChannel channel = file.getChannel();
            ByteBuffer a = ByteBuffer.allocate(3);
            ByteBuffer b = ByteBuffer.allocate(3);
            ByteBuffer c = ByteBuffer.allocate(5);
            channel.read(new ByteBuffer[]{a, b, c});
            a.flip();
            b.flip();
            c.flip();
            System.out.println(print(a));
            System.out.println(print(b));
            System.out.println(print(c));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 分散写
     */
    static void scatteringWrite() {
        try (RandomAccessFile file = new RandomAccessFile("/Users/panhongtong/mydir/text.txt", "rw")) {
            FileChannel channel = file.getChannel();
            ByteBuffer d = ByteBuffer.allocate(4);
            ByteBuffer e = ByteBuffer.allocate(4);
            channel.position(11);

            d.put(new byte[]{'f', 'o', 'u', 'r'});
            e.put(new byte[]{'f', 'i', 'v', 'e'});
            d.flip();
            e.flip();
            System.out.println(print(d));
            System.out.println(print(e));
            channel.write(new ByteBuffer[]{d, e});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String print(ByteBuffer b){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i =0 ; i< b.limit();i++){
            stringBuilder.append((char)b.get(i));
        }
        return stringBuilder.toString();
    }

}
