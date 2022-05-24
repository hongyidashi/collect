package com.collect.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @description: 事务接口
 * @author: panhongtong
 * @date: 2022/5/22 10:24
 **/
public interface Transaction {

    Connection getConnection() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;

}
