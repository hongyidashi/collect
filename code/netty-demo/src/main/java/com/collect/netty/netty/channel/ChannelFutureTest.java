package com.collect.netty.netty.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @description: Channel
 * @author: panhongtong
 * @date: 2022/4/26 10:05
 **/
public class ChannelFutureTest {

    public static void main(String[] args) throws Exception {
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) {

                    }
                })
                .connect("localhost", 8080);

        System.out.println(channelFuture.channel());

        //同步等待连接
        channelFuture.sync();
        System.out.println(channelFuture.channel());
    }
}

