package com.collect.mybatis.session;

import com.collect.mybatis.binding.MapperRegistry;
import com.collect.mybatis.mapping.MappedStatement;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 配置项
 * @author: panhongtong
 * @date: 2022/5/20 08:56
 **/
public class Configuration {

    private final MapperRegistry mapperRegistry = new MapperRegistry(this);

    /**
     * 映射的语句，存在Map里 Map<MappedStatement.id, MappedStatement>
     */
    private final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }
}
