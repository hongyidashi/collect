package org.collect.registry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 描述: 启动类
 *
 * @author: panhongtong
 * @date: 2021-05-02 11:13
 **/
@Slf4j
@SpringBootApplication
public class RegistryApplication {
  public static void main(String[] args) {
    SpringApplication.run(RegistryApplication.class);
    log.info("\n 注册中心启动成功 (#^.^#)");
  }
}
