package com.collection.mybatis.test;

import com.collect.mybatis.io.Resources;
import com.collect.mybatis.session.SqlSession;
import com.collect.mybatis.session.SqlSessionFactory;
import com.collect.mybatis.session.SqlSessionFactoryBuilder;
import com.collection.mybatis.test.dao.IUserDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

/**
 * @description: 测试
 * @author: panhongtong
 * @date: 2022/5/17 22:46
 **/
@Slf4j
public class ApiTest {

    @Test
    public void testSqlSessionFactory() throws IOException {
        // 1. 从SqlSessionFactory中获取SqlSession
        Reader reader = Resources.getResourceAsReader("mybatis-config-datasource.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 2. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 3. 测试验证
        String res = userDao.queryUserInfoById("10001");
        log.info("测试结果：{}", res);
    }

}
