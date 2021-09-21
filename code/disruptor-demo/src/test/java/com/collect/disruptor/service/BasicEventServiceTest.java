package com.collect.disruptor.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 描述: 测试
 *
 * @author: panhongtong
 * @date: 2021-09-21 22:50
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BasicEventServiceTest {

    @Autowired
    BasicEventService basicEventService;

    @Test
    public void publish() throws InterruptedException {
        log.info("start publish test");

        int count = 100;

        for(int i=0;i<count;i++) {
            log.info("publish {}", i);
            basicEventService.publish(String.valueOf(i));
        }

        // 异步消费，因此需要延时等待
        Thread.sleep(1000);
        // 消费的事件总数应该等于发布的事件数
        Assert.assertEquals(count, basicEventService.eventCount());
    }

}
