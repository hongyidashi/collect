package com.collect.websocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述: netty服务
 * 作者: panhongtong
 * 创建时间: 2020-12-28 11:18
 **/
@Slf4j
public class MyServer {

    /**
     * 启动服务
     */
    public static void startServer() throws InterruptedException {
        // 创建两个组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 启动引导类
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    // 采用NIO
                    .channel(NioServerSocketChannel.class)
                    // 设置日志级别
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // 在此设置自定义处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //websocket是基于http协议的，所以要加入这个
                            pipeline.addLast(new HttpServerCodec());
                            //以块的方式传输，所以要加入ChunkedWriteHandler
                            pipeline.addLast(new ChunkedWriteHandler());
                            //http传输过程中是分段的，HttpObjectAggregator可以将多个分段聚合
                            //这也解释了传输大量数据时会分为两个http请求
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            //websocket以帧(frame)的形式传递
                            //WebSocketServerProtocolHandler的核心功能是将http协议升级为ws协议，长链接
                            pipeline.addLast(new WebSocketServerProtocolHandler("/MDZZ"));
                            //自定义处理器
                            pipeline.addLast(new MyTextWebSocketFrameHandler());
                        }
                    });
            log.info("netty 服务器启动...");
            ChannelFuture cf = bootstrap.bind(10086).sync();
            cf.channel().closeFuture().sync();
        } finally {
            // 优雅的关闭~优雅
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
