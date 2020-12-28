package com.collect.websocket;

import com.collect.websocket.server.MyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 描述: 启动类
 * 作者: panhongtong
 * 创建时间: 2020-12-27 22:50
 **/
@SpringBootApplication
public class WebSocketApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(WebSocketApplication.class);
        MyServer.startServer();
    }
}
