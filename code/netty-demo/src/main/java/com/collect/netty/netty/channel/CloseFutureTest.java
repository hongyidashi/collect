package com.collect.netty.netty.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @description: CloseFuture
 * @author: panhongtong
 * @date: 2022/4/26 10:09
 **/
public class CloseFutureTest {
    public static void main(String[] args) throws Exception {
        // 将group提出来，不能匿名方式，为了后面调动shutdownGracefully()方法
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("localhost", 8080);

        // 同步等待连接
        Channel channel = channelFuture.sync().channel();

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if ("q".equals(line)) {
                    System.out.println("关闭channel");
                    // close 异步操作 1s 之后
                    channel.close();
                    break;
                }
                channel.writeAndFlush(line);
            }
        }, "input").start();


        // 处理channel关闭后的操作
        // 获取 CloseFuture 对象， 1) 同步处理关闭， 2) 异步处理关闭
        ChannelFuture closeFuture = channel.closeFuture();

        //同步
        //closeFuture.sync();

        //异步 - EventLoopGroup线程未关闭
        //closeFuture.addListener((ChannelFutureListener) future -> System.out.println("处理channel关闭后的操作"));

        //异步 - EventLoopGroup线程优雅关闭
        closeFuture.addListener((ChannelFutureListener) future -> group.shutdownGracefully());
    }
}
