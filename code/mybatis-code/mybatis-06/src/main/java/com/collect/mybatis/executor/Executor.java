package com.collect.mybatis.executor;

import com.collect.mybatis.mapping.BoundSql;
import com.collect.mybatis.mapping.MappedStatement;
import com.collect.mybatis.session.ResultHandler;
import com.collect.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

/**
 * @description: 执行器
 * @author: panhongtong
 * @date: 2022/5/26 14:28
 **/
public interface Executor {

    ResultHandler NO_RESULT_HANDLER = null;

    <E> List<E> query(MappedStatement mappedStatement, Object parameter,
                      ResultHandler resultHandler, BoundSql boundSql);

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);

}
