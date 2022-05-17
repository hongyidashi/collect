package com.collection.mybatis.dao;

/**
 * @description: 测试Dao
 * @author: panhongtong
 * @date: 2022/5/17 22:44
 **/
public interface IUserDao {

    /**
     * 查询用户名
     *
     * @param userId userId
     * @return name
     */
    String queryUserName(String userId);

    /**
     * 查询用户年龄
     *
     * @param userId userId
     * @return age
     */
    Integer queryUserAge(String userId);

}
