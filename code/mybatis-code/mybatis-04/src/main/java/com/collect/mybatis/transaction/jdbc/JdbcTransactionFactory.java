package com.collect.mybatis.transaction.jdbc;

import com.collect.mybatis.session.TransactionIsolationLevel;
import com.collect.mybatis.transaction.Transaction;
import com.collect.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @description: JdbcTransaction 工厂
 * @author: panhongtong
 * @date: 2022/5/22 10:30
 **/
public class JdbcTransactionFactory implements TransactionFactory {

    @Override
    public Transaction newTransaction(Connection conn) {
        return new JdbcTransaction(conn);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(dataSource, level, autoCommit);
    }

}
