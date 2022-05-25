package com.collect.mybatis.session;

/**
 * @description: SqlSessionFactory
 * @author: panhongtong
 * @date: 2022/5/19 08:40
 **/
public interface SqlSessionFactory {

    /**
     * 打开一个 session
     *
     * @return SqlSession
     */
    SqlSession openSession();

}
