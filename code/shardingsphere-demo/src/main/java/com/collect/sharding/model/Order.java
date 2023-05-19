package com.collect.sharding.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @description: 订单
 * @author: panhongtong
 * @date: 2023/5/19 10:45
 **/
@Data
public class Order {

    private Long orderId;

    private Long userId;

    private BigDecimal totalPrice;

    private Integer state;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
    
}
