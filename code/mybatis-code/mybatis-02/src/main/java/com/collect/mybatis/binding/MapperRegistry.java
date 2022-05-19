package com.collect.mybatis.binding;

import cn.hutool.core.lang.ClassScanner;
import com.collect.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @description: mapper 注册
 * @author: panhongtong
 * @date: 2022/5/18 22:31
 **/
public class MapperRegistry {

    /**
     * 代理类 <--> 代理工厂
     */
    private static final Map<Class<?>, MapperProxyFactory<?>> FACTORY_MAP = new HashMap<>();

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        MapperProxyFactory<T> factory = (MapperProxyFactory<T>) FACTORY_MAP.get(type);
        if (factory == null) {
            throw new RuntimeException(String.format("类型为 %s 的mapper未注册", type));
        }

        return factory.newInstance(sqlSession);
    }

    public <T> void addMapper(Class<T> type) {
        // Mapper必须是接口
        if (type.isInterface()) {
            FACTORY_MAP.computeIfAbsent(type, t -> {
                throw new RuntimeException(String.format("已存在类型:%s", t));
            });
            FACTORY_MAP.put(type, new MapperProxyFactory<>(type));
        }
    }

    public void addMappers(String packageName) {
        Set<Class<?>> mapperSet = ClassScanner.scanPackage(packageName);
        for (Class<?> mapperClass : mapperSet) {
            addMapper(mapperClass);
        }
    }

}
