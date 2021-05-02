package org.collect.server1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 描述: 启动类
 *
 * @author: panhongtong
 * @date: 2021-05-02 20:33
 **/
@ComponentScan(basePackages = "org.collect.dependency.**")
@SpringBootApplication
public class Server1Application {
    public static void main(String[] args) {
        SpringApplication.run(Server1Application.class);
    }
}
