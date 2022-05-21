package com.collection.mybatis.test.model;

import lombok.Data;

import java.util.Date;

/**
 * @description: user
 * @author: panhongtong
 * @date: 2022/5/20 09:30
 **/
@Data
public class User {

    private Long id;
    private String userId;
    private String userHead;
    private Date createTime;
    private Date updateTime;

}
