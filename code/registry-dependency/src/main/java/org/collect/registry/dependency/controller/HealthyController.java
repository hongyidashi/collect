package org.collect.registry.dependency.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述: 健康检查控制器
 *
 * @author: panhongtong
 * @date: 2021-05-02 21:31
 **/
@RestController
public class HealthyController {

    @GetMapping("/registry/healthy")
    public String healthy() {
        return "healthy";
    }

}
