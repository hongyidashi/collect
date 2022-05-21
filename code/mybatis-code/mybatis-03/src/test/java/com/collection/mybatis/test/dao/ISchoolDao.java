package com.collection.mybatis.test.dao;

/**
 * @description:
 * @author: panhongtong
 * @date: 2022/5/19 08:42
 **/
public interface ISchoolDao {

    /**
     * 查询学校名
     *
     * @param userId userId
     * @return 学校名
     */
    String querySchoolName(String userId);
    
}
