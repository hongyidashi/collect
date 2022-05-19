package com.collection.mybatis.test;

import com.collect.mybatis.binding.MapperRegistry;
import com.collect.mybatis.session.SqlSession;
import com.collect.mybatis.session.SqlSessionFactory;
import com.collect.mybatis.session.defaults.DefaultSqlSessionFactory;
import com.collection.mybatis.test.dao.IUserDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @description:
 * @author: panhongtong
 * @date: 2022/5/17 22:46
 **/
@Slf4j
public class ApiTest {

    @Test
    public void testMapperProxyFactory() {
        // 1. 注册 Mapper
        MapperRegistry registry = new MapperRegistry();
        registry.addMappers("com.collection.mybatis.test.dao");

        // 2. 从 SqlSession 工厂获取 Session
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(registry);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 3. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        // 4. 测试验证
        String res = userDao.queryUserName("10001");
        log.info("测试结果：{}", res);
    }

}
