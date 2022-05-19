package com.collect.mybatis.session.defaults;

import com.collect.mybatis.binding.MapperRegistry;
import com.collect.mybatis.session.SqlSession;
import com.collect.mybatis.session.SqlSessionFactory;

/**
 * @description: DefaultSqlSessionFactory
 * @author: panhongtong
 * @date: 2022/5/19 08:41
 **/
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final MapperRegistry mapperRegistry;

    public DefaultSqlSessionFactory(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(mapperRegistry);
    }
}
