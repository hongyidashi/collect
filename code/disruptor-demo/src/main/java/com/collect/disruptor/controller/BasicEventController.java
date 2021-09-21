package com.collect.disruptor.controller;

import com.collect.disruptor.service.BasicEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 描述: 入门-控制器
 *
 * @author: panhongtong
 * @date: 2021-09-21 22:46
 **/
@RequestMapping("/basic")
@RestController
public class BasicEventController {

    @Autowired
    private BasicEventService basicEventService;

    @GetMapping(value = "/{value}")
    public String publish(@PathVariable("value") String value) {
        basicEventService.publish(value);
        return "success, " + LocalDateTime.now().toString();
    }

}
