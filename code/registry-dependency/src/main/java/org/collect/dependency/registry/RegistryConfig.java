package org.collect.dependency.registry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述: 注册配置类
 *
 * @author: panhongtong
 * @date: 2021-05-02 21:37
 **/
@Configuration
public class RegistryConfig {

    @Bean
    public RegistryRunner myRegistryRunner() {
        return new RegistryRunner();
    }
}
