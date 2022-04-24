package com.collect.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @description: FileChannel演示
 * @author: panhongtong
 * @date: 2022/4/22 16:51
 **/
public class FileChannelDemo {

    public static void main(String[] args) throws Exception {
//        getByFileInputStream();
//        getByFileOutputStream();
//        getByRandomAccessFile();
//        fileChannelCopy();
        fileChannelCopyMax();
    }

    /**
     * 数据传输，超过最大值时要分配传输
     */
    static void fileChannelCopyMax() {
        long start = System.currentTimeMillis();
        String sPath = "/Users/panhongtong/mydir/text.txt";
        String tPath = "/Users/panhongtong/mydir/text-copy.txt";

        try (FileInputStream fi = new FileInputStream(sPath);
             FileOutputStream fo = new FileOutputStream(tPath);
             // 得到对应的文件通道
             FileChannel in = fi.getChannel();
             // 得到对应的文件通道
             FileChannel out = fo.getChannel();
        ) {
            // 总文件大小
            long size = in.size();
            // left 剩余文件的数量
            for (long left = size; left > 0; ) {
                System.out.println("position = " + (size - left) + "，left = " + left);
                // transferTo返回传输的数量，剩余的减去传输的，就是当前剩余的数量
                left -= in.transferTo((size - left), left, out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("用时为：" + (end - start) + "ms");
    }

    /**
     * 数据传输
     */
    static void fileChannelCopy() {
        long start = System.currentTimeMillis();
        String sPath = "/Users/panhongtong/mydir/text.txt";
        String tPath = "/Users/panhongtong/mydir/text-copy.txt";

        try (FileInputStream fi = new FileInputStream(sPath);
             FileOutputStream fo = new FileOutputStream(tPath);
             // 得到对应的文件通道
             FileChannel in = fi.getChannel();
             // 得到对应的文件通道
             FileChannel out = fo.getChannel();
        ) {
            // 连接两个通道，并且从in通道读取，然后写入out通道
            in.transferTo(0, in.size(), out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("用时为：" + (end - start) + "ms");
    }

    /**
     * 根据RandomAccessFile获取
     */
    static void getByRandomAccessFile() {
        // 使用RandomAccessFile获取channel
        try (RandomAccessFile file = new RandomAccessFile("/Users/panhongtong/mydir/text.txt", "rw")) {
            FileChannel channel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(15);
            //读取文件内容到buffer
            channel.read(buffer);
            buffer.flip();
            System.out.println(print(buffer));

            // 切换为写模式，并且清空buffer
            buffer.clear();
            //写入helloworld到文件
            buffer.put(StandardCharsets.UTF_8.encode("helloworld"));
            buffer.flip();
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据FileOutputStream获取
     */
    static void getByFileOutputStream() {
        // 使用FileOutputStream获取channel
        try (FileOutputStream fileOutputStream = new FileOutputStream("/Users/panhongtong/mydir/text.txt", true)) {
            FileChannel channel = fileOutputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(10);
            buffer.put(StandardCharsets.UTF_8.encode("helloworld"));
            buffer.flip();
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据FileInputStream获取
     */
    static void getByFileInputStream() {
        // 使用FileInputStream获取channel
        try (FileInputStream fileInputStream = new FileInputStream("/Users/panhongtong/mydir/text.txt")) {
            FileChannel channel = fileInputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(10);
            channel.read(buffer);
//            channel.write(buffer);
            buffer.flip();
            System.out.println((print(buffer)));
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
