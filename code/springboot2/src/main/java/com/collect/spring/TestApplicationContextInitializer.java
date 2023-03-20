package com.collect.spring;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 测试初始化器
 * @author: panhongtong
 * @date: 2023/3/20 11:06
 **/
public class TestApplicationContextInitializer implements ApplicationContextInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Map<String, Object> map = new HashMap<>(4);
        map.put("server.port", 35004);
        MapPropertySource mapPropertySource = new MapPropertySource("mytest", map);
        environment.getPropertySources().addLast(mapPropertySource);
    }
}
