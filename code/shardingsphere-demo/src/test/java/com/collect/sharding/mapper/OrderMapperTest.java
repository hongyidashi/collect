package com.collect.sharding.mapper;

import cn.hutool.json.JSONUtil;
import com.collect.sharding.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: panhongtong
 * @date: 2023/5/19 10:49
 **/
@SpringBootTest
public class OrderMapperTest {

    @Resource
    private OrderMapper orderMapper;

    @Test
    public void testInsert() {
        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setUserId(1L);
            order.setState(0);
            order.setTotalPrice(new BigDecimal((i + 1) * 5));
            order.setCreateTime(LocalDateTime.now());
            order.setUpdateTime(order.getCreateTime());
            orderMapper.insert(order);
        }
    }

    @Test
    public void testSelectList() {
        List<Long> idList = Arrays.asList(866270978408185856L, 866270978936668161L);
        List<Order> orderList = orderMapper.selectListByIds(idList);
        System.out.println(JSONUtil.toJsonStr(orderList));
    }


}
