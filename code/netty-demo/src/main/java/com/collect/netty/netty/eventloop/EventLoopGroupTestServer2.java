package com.collect.netty.netty.eventloop;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @description: EventLoopGroup
 * @author: panhongtong
 * @date: 2022/4/26 09:21
 **/
public class EventLoopGroupTestServer2 {

    public static void main(String[] args) throws InterruptedException {
        // 定义一个defaultEventLoopGroup，对职责进一步划分
        DefaultEventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup();

        new ServerBootstrap()
                .group(new NioEventLoopGroup(1), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        // 解码处理器
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
                                System.out.println(Thread.currentThread().getName() + ": " + s);
                                // 要想使下了一个处理器，能够收到此处理器的结果，需要使用西面这个方法传递
                                channelHandlerContext.fireChannelRead(s);
                            }
                            // 添加另一个处理器，使用额外的EventLoopGroup
                        }).addLast(defaultEventLoopGroup, "otherHandler", new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
                                System.out.println(Thread.currentThread().getName() + ": " + s);
                            }
                        });
                    }
                }).bind(8080).sync();
    }

}
