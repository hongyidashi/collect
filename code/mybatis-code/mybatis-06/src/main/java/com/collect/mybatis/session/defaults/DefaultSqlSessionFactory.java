package com.collect.mybatis.session.defaults;

import com.collect.mybatis.executor.Executor;
import com.collect.mybatis.mapping.Environment;
import com.collect.mybatis.session.Configuration;
import com.collect.mybatis.session.SqlSession;
import com.collect.mybatis.session.SqlSessionFactory;
import com.collect.mybatis.session.TransactionIsolationLevel;
import com.collect.mybatis.transaction.Transaction;
import com.collect.mybatis.transaction.TransactionFactory;

import java.sql.SQLException;

/**
 * @description: DefaultSqlSessionFactory
 * @author: panhongtong
 * @date: 2022/5/19 08:41
 **/
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        Transaction tx = null;
        try {
            final Environment environment = configuration.getEnvironment();
            TransactionFactory transactionFactory = environment.getTransactionFactory();
            tx = transactionFactory.newTransaction(configuration.getEnvironment().getDataSource(),
                    TransactionIsolationLevel.READ_COMMITTED, false);
            // 创建执行器
            final Executor executor = configuration.newExecutor(tx);
            // 创建DefaultSqlSession
            return new DefaultSqlSession(configuration, executor);
        } catch (Exception e) {
            try {
                assert tx != null;
                tx.close();
            } catch (SQLException ignore) {
            }
            throw new RuntimeException("Error opening session.  Cause: " + e);
        }
    }
}
