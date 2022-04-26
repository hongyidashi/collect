package com.collect.netty.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Date;

/**
 * @description: HelloWorld
 * @author: panhongtong
 * @date: 2022/4/25 23:54
 **/
public class HelloWorldClient {

    public static void main(String[] args) throws InterruptedException {
        new Bootstrap()
                // 1、创建 NioEventLoopGroup，可以简单理解为 `线程池 + Selector`
                .group(new NioEventLoopGroup())
                // 2、选择服务 ServerSocketChannel 实现类，这里选择Nio
                .channel(NioSocketChannel.class)
                // 3、此处是给客户端SocketChannel使用，ChannelInitializer执行一次，待客户端建立连接后，执行initChannel，添加更多的处理器
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) {
                        // 8、消息会经过通道 handler 处理，这里是将 String => ByteBuf 发出
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 4、指定要连接的服务器端口
                .connect("127.0.0.1", 8080)
                // 5、同步方法，等待connect()连接完毕
                .sync()
                // 6、获取channel对象，即通道，可读写操作
                .channel()
                // 7、写入消息并清空缓冲区
                .writeAndFlush(new Date() + ": hello world!");
    }

}
