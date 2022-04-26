package com.collect.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @description: 读事件
 * @author: panhongtong
 * @date: 2022/4/24 14:54
 **/
public class ReadEventServerTest {
    public static void main(String[] args) {
        try (ServerSocketChannel channel = ServerSocketChannel.open();
             Selector selector = Selector.open();) {
            
            channel.bind(new InetSocketAddress(8080));
            System.out.println(channel);
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                int count = selector.select();
                System.out.println("select count:" + count);

                // 获取所有事件
                Set<SelectionKey> keys = selector.selectedKeys();

                // 遍历所有事件，逐一处理
                Iterator<SelectionKey> iter = keys.iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    // 判断事件类型
                    if (key.isAcceptable()) {
                        ServerSocketChannel c = (ServerSocketChannel) key.channel();
                        // 必须处理
                        SocketChannel sc = c.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                        System.out.println("连接已建立:" + sc);
                    } else if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(128);
                        int read = sc.read(buffer);
                        if (read == -1) {
                            key.cancel();
                            sc.close();
                        } else {
                            buffer.flip();
                            System.out.println(print(buffer));
                        }
                    }
                    // 处理完毕，必须将事件移除
                    iter.remove();
                }
            }
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

