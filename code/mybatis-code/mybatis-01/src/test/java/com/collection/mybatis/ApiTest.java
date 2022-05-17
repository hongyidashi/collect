package com.collection.mybatis;

import com.collect.mybatis.binding.MapperProxyFactory;
import com.collection.mybatis.dao.IUserDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: panhongtong
 * @date: 2022/5/17 22:46
 **/
@Slf4j
public class ApiTest {

    @Test
    public void testMapperProxyFactory() {
        MapperProxyFactory<IUserDao> mapperProxyFactory = new MapperProxyFactory<>(IUserDao.class);
        Map<String, String> sqlSession = new HashMap<>(4);
        sqlSession.put("com.collection.mybatis.dao.IUserDao.queryUserName", "模拟执行查询用户名");
        sqlSession.put("com.collection.mybatis.dao.IUserDao.queryUserAge", "模拟执行查询用户年龄");
        IUserDao userDao = mapperProxyFactory.newInstance(sqlSession);
        String result = userDao.queryUserName("123");
        log.info("result:{}", result);
    }
}
