package org.collect.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 描述: 启动类
 * 作者: panhongtong
 * 创建时间: 2021-01-13 22:58
 **/
@MapperScan("org.collect.**.mapper")
@SpringBootApplication
public class SecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class);
    }
}
