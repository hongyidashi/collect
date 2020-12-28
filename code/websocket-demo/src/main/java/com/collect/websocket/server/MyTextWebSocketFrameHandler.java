package com.collect.websocket.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * 描述: 自定义处理器
 * 作者: panhongtong
 * 创建时间: 2020-12-28 11:27
 **/
@Slf4j
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 处理逻辑
     * @param channelHandlerContext
     * @param textWebSocketFrame
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        log.info("收到客户端消息消息：" + textWebSocketFrame.text());
        //回复服务器消息
        channelHandlerContext.channel()
                .writeAndFlush(new TextWebSocketFrame(LocalDateTime.now() + ":" + textWebSocketFrame.text()));
    }

    /**
     * 建立连接时会触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //id、LongText唯一，ShortText不唯一
        log.info(ctx.channel().id().asLongText());
        log.info(ctx.channel().id().asShortText());
    }

    /**
     * 发生异常时会触发
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
