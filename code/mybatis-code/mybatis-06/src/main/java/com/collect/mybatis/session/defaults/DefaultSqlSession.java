package com.collect.mybatis.session.defaults;

import com.collect.mybatis.executor.Executor;
import com.collect.mybatis.mapping.MappedStatement;
import com.collect.mybatis.session.Configuration;
import com.collect.mybatis.session.SqlSession;

import java.util.List;

/**
 * @description: 默认SqlSession
 * @author: panhongtong
 * @date: 2022/5/19 08:38
 **/
public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <T> T selectOne(String statement) {
        return this.selectOne(statement, null);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        MappedStatement ms = configuration.getMappedStatement(statement);
        List<T> list = executor.query(ms, parameter, Executor.NO_RESULT_HANDLER, ms.getBoundSql());
        return list.get(0);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
