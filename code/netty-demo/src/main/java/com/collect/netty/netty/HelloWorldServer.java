package com.collect.netty.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @description: HelloWorld
 * @author: panhongtong
 * @date: 2022/4/25 23:50
 **/
public class HelloWorldServer {

    public static void main(String[] args) {
        new ServerBootstrap()
                // 1、创建 NioEventLoopGroup，可以简单理解为 `线程池 + Selector`
                .group(new NioEventLoopGroup())
                // 2、选择服务 ServerSocketChannel 实现类，这里选择Nio
                .channel(NioServerSocketChannel.class)
                // 3、此处是给客户端SocketChannel使用，ChannelInitializer执行一次，待客户端建立连接后，执行initChannel，添加更多的处理器
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        // 5、客户端SocketChannel处理器，解码：ByteBuffer -> String
                        ch.pipeline().addLast(new StringDecoder());
                        // 6、客户端SocketChannel业务处理器，使用上一个处理器的结果
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                // 4、服务端ServerSocketChannel绑定监听端口
                .bind(8080);
    }

}
