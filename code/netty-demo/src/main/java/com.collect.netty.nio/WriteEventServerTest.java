package com.collect.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @description: 写事件
 * @author: panhongtong
 * @date: 2022/4/25 10:08
 **/
public class WriteEventServerTest {

    public static void main(String[] args) {
        try (// 开启一个服务channel
             ServerSocketChannel ssc = ServerSocketChannel.open();
             //初始化selector
             Selector selector = Selector.open()
        ) {
            // 设置非阻塞
            ssc.configureBlocking(false);
            // 绑定端口
            ssc.bind(new InetSocketAddress(8080));


            //注册服务端到selector
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                // 监听事件，此处会阻塞
                selector.select();

                // 获取所有事件key，遍历
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    // 不管处理成功与否，移除key
                    iter.remove();
                    // 如果是建立连接事件
                    if (key.isAcceptable()) {
                        // 处理accept事件
                        SocketChannel sc = ssc.accept();
                        // 设置非阻塞
                        sc.configureBlocking(false);
                        // 此处是第一阶段
                        // 注册一个read事件到selector
                        SelectionKey sckey = sc.register(selector, SelectionKey.OP_READ);
                        // 1. 向客户端发送内容
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < 30000000; i++) {
                            sb.append("a");
                        }
                        // 字符串转buffer
                        ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());
                        // 2. 写入数据到客户端channel
                        int write = sc.write(buffer);
                        // 3. write 表示实际写了多少字节
                        System.out.println("实际写入字节:" + write);
                        // 4. 如果有剩余未读字节，才需要关注写事件
                        // 此处是第二阶段
                        if (buffer.hasRemaining()) {
                            // 在原有关注事件的基础上，多关注 写事件
                            sckey.interestOps(sckey.interestOps() + SelectionKey.OP_WRITE);
                            // 把 buffer 作为附件加入 sckey
                            sckey.attach(buffer);
                        }
                    } else if (key.isWritable()) {
                        // 检索key中的附件buffer
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        // 获取客户端channel
                        SocketChannel sc = (SocketChannel) key.channel();
                        // 根据上次的position继续写
                        int write = sc.write(buffer);
                        System.out.println("实际写入字节:" + write);
                        // 如果写完了，需要将绑定的附件buffer去掉，并且去掉写事件
                        // 如果没写完将会继续while，执行写事件，知道完成为止
                        if (!buffer.hasRemaining()) {
                            key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
                            key.attach(null);
                        }
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
