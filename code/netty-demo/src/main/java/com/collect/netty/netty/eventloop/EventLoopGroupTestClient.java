package com.collect.netty.netty.eventloop;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @description: EventLoopGroup
 * @author: panhongtong
 * @date: 2022/4/26 09:13
 **/
public class EventLoopGroupTestClient {

    public static void main(String[] args) throws InterruptedException {
        Channel channel = new Bootstrap()
                .group(new NioEventLoopGroup(1))
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        System.out.println("init...");
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .channel(NioSocketChannel.class).connect("localhost", 8080)
                .sync()
                .channel();

        channel.writeAndFlush("aaa");
        Thread.sleep(1000);
        channel.writeAndFlush("aaa");
    }

}
