package com.collect;

import com.collect.spring.TestApplicationContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description: 启动类
 * @author: panhongtong
 * @date: 2023/3/20 11:05
 **/
@SpringBootApplication
public class SpringApp {

    public static void main(String[] args) {
//        SpringApplication.run(SpringApp.class, args);
        SpringApplication application = new SpringApplication(SpringApp.class);
        application.addInitializers(new TestApplicationContextInitializer());
        application.run(args);
    }

}
