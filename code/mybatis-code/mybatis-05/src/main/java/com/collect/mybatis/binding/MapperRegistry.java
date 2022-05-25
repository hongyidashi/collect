package com.collect.mybatis.binding;

import com.collect.mybatis.session.Configuration;
import com.collect.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: mapper 注册
 * @author: panhongtong
 * @date: 2022/5/18 22:31
 **/
public class MapperRegistry {

    private Configuration config;

    public MapperRegistry(Configuration config) {
        this.config = config;
    }

    /**
     * 将已添加的映射器代理加入到 HashMap
     */
    private static final Map<Class<?>, MapperProxyFactory<?>> FACTORY_MAP = new HashMap<>();

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) FACTORY_MAP.get(type);
        if (mapperProxyFactory == null) {
            throw new RuntimeException("Type " + type + " is not known to the MapperRegistry.");
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new RuntimeException("Error getting mapper instance. Cause: " + e, e);
        }
    }

    public <T> void addMapper(Class<T> type) {
        /* Mapper 必须是接口才会注册 */
        if (type.isInterface()) {
            if (hasMapper(type)) {
                // 如果重复添加了，报错
                throw new RuntimeException("Type " + type + " is already known to the MapperRegistry.");
            }
            // 注册映射器代理工厂
            FACTORY_MAP.put(type, new MapperProxyFactory<>(type));
        }
    }

    public <T> boolean hasMapper(Class<T> type) {
        return FACTORY_MAP.containsKey(type);
    }

}
