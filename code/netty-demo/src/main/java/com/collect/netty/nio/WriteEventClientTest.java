package com.collect.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @description: 写事件
 * @author: panhongtong
 * @date: 2022/4/25 10:54
 **/
public class WriteEventClientTest {

    public static void main(String[] args) {
        try (// 开启selector
             Selector selector = Selector.open();
             // 开启客户端channel
             SocketChannel sc = SocketChannel.open();) {

            //设置非阻塞
            sc.configureBlocking(false);
            // 注册一个连接事件和读事件
            sc.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ);
            //连接到服务端
            sc.connect(new InetSocketAddress("localhost", 8080));
            int count = 0;
            while (true) {
                //此处监测事件，阻塞
                selector.select();
                //获取时间key集合，并遍历
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    //无论成功与否，都要移除
                    iter.remove();
                    //连接
                    if (key.isConnectable()) {
                        System.out.println(sc.finishConnect());
                    } else if (key.isReadable()) {
                        //分配内存buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
                        //读数据到buffer
                        count += sc.read(buffer);
                        //清空buffer
                        buffer.clear();
                        //打印总字节数
                        System.out.println(count);
                    }
                }
            }
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

}
