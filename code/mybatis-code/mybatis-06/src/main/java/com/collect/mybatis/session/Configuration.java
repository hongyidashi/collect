package com.collect.mybatis.session;

import com.collect.mybatis.binding.MapperRegistry;
import com.collect.mybatis.datasource.druid.DruidDataSourceFactory;
import com.collect.mybatis.datasource.pooled.PooledDataSourceFactory;
import com.collect.mybatis.datasource.unpooled.UnPooledDataSourceFactory;
import com.collect.mybatis.executor.Executor;
import com.collect.mybatis.executor.SimpleExecutor;
import com.collect.mybatis.executor.resultset.DefaultResultSetHandler;
import com.collect.mybatis.executor.resultset.ResultSetHandler;
import com.collect.mybatis.executor.statement.PreparedStatementHandler;
import com.collect.mybatis.executor.statement.StatementHandler;
import com.collect.mybatis.mapping.BoundSql;
import com.collect.mybatis.mapping.Environment;
import com.collect.mybatis.mapping.MappedStatement;
import com.collect.mybatis.transaction.Transaction;
import com.collect.mybatis.transaction.jdbc.JdbcTransactionFactory;
import com.collect.mybatis.type.TypeAliasRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 配置项
 * @author: panhongtong
 * @date: 2022/5/20 08:56
 **/
public class Configuration {

    /**
     * 环境
     */
    protected Environment environment;

    protected final MapperRegistry mapperRegistry = new MapperRegistry(this);

    /**
     * 映射的语句，存在Map里 Map<MappedStatement.id, MappedStatement>
     */
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    /**
     * 类型别名注册机
     */
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);

        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnPooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);

    }

    /**
     * 创建结果集处理器
     */
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor, mappedStatement, boundSql);
    }

    /**
     * 创建语句处理器
     */
    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        return new PreparedStatementHandler(executor, mappedStatement, parameter, resultHandler, boundSql);
    }

    /**
     * 生产执行器
     */
    public Executor newExecutor(Transaction transaction) {
        return new SimpleExecutor(this, transaction);
    }

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

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
