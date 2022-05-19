package com.collect.mybatis.binding;

import com.collect.mybatis.session.SqlSession;

import java.lang.reflect.Proxy;

/**
 * @description: mapper代理工厂
 * @author: panhongtong
 * @date: 2022/5/17 22:39
 **/
public class MapperProxyFactory<T> {

    private final Class<T> mapperInterface;

    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public T newInstance(SqlSession sqlSession) {
        final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface);
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }

}
