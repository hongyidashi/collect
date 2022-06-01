package com.collect.mybatis.executor;

import com.collect.mybatis.mapping.BoundSql;
import com.collect.mybatis.mapping.MappedStatement;
import com.collect.mybatis.session.Configuration;
import com.collect.mybatis.session.ResultHandler;
import com.collect.mybatis.transaction.Transaction;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;

/**
 * @description: 执行器抽象基类
 * @author: panhongtong
 * @date: 2022/5/26 15:08
 **/
@Slf4j
public abstract class BaseExecutor implements Executor {

    protected Configuration configuration;
    protected Transaction transaction;
    protected Executor wrapper;

    private boolean closed;

    public BaseExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
        this.wrapper = this;
        closed = false;
    }

    @Override
    public <E> List<E> query(MappedStatement mappedStatement, Object parameter,
                             ResultHandler resultHandler, BoundSql boundSql) {
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }

        return doQuery(mappedStatement, parameter, resultHandler, boundSql);
    }

    protected abstract <E> List<E> doQuery(MappedStatement mappedStatement, Object parameter,
                                           ResultHandler resultHandler, BoundSql boundSql);

    @Override
    public Transaction getTransaction() {
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }

        return transaction;
    }

    @Override
    public void commit(boolean required) throws SQLException {
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }

        if (required) {
            transaction.commit();
        }
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        if (!closed && required) {
            transaction.rollback();
        }
    }

    @Override
    public void close(boolean forceRollback) {
        try {
            try {
                rollback(forceRollback);
            } finally {
                transaction.close();
            }
        } catch (SQLException e) {
            log.error("Unexpected exception on closing transaction.  Cause: ", e);
        } finally {
            transaction = null;
            closed = true;
        }
    }
}
