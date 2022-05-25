package com.collect.mybatis.mapping;

import com.collect.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;

/**
 * @description: 环境
 * @author: panhongtong
 * @date: 2022/5/22 10:38
 **/
public final class Environment {

    /**
     * 环境id
     */
    private final String id;
    /**
     * 事务工厂
     */
    private final TransactionFactory transactionFactory;
    /**
     * 数据源
     */
    private final DataSource dataSource;

    public Environment(String id, TransactionFactory transactionFactory, DataSource dataSource) {
        this.id = id;
        this.transactionFactory = transactionFactory;
        this.dataSource = dataSource;
    }

    public static class Builder {

        private final String id;
        private TransactionFactory transactionFactory;
        private DataSource dataSource;

        public Builder(String id) {
            this.id = id;
        }

        public Builder transactionFactory(TransactionFactory transactionFactory) {
            this.transactionFactory = transactionFactory;
            return this;
        }

        public Builder dataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public String id() {
            return this.id;
        }

        public Environment build() {
            return new Environment(this.id, this.transactionFactory, this.dataSource);
        }

    }

    public String getId() {
        return id;
    }

    public TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

}
